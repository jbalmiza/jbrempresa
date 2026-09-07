package com.jbrempresa.backend.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.jbrempresa.backend.dto.ventas.CatalogoDtos;
import com.jbrempresa.backend.entity.*;
import com.jbrempresa.backend.repository.*;

@Service
public class CatalogoService {
    public static final String MODULO = "PRODUCTOS";
    public static final String PUBLICADO = "CATALOGO_PUBLICADO";
    public static final String DOMICILIO = "CATALOGO_DOMICILIO";
    public static final String TOKEN = "CATALOGO_TOKEN_GENERAL";
    private final ParametroRepository parametros; private final EmpresaRepository empresas;
    private final ProductoRepository productos; private final ServicioRepository servicios; private final PersonaRepository personas;
    private final DocumentoVentaRepository documentos; private final DocumentoVentaDetalleRepository detalles;
    private final CatalogoPosicionRepository posiciones; private final NumeradorDocumentoVentaService numerador;
    private final ProductoService productoService; private final MallaService mallaService;
    private final TipoArticuloRepository tiposArticulo;

    public CatalogoService(ParametroRepository parametros, EmpresaRepository empresas, ProductoRepository productos, ServicioRepository servicios,
            PersonaRepository personas, DocumentoVentaRepository documentos, DocumentoVentaDetalleRepository detalles,
            CatalogoPosicionRepository posiciones, NumeradorDocumentoVentaService numerador,
            ProductoService productoService, MallaService mallaService, TipoArticuloRepository tiposArticulo) {
        this.parametros=parametros;this.empresas=empresas;this.productos=productos;this.servicios=servicios;this.personas=personas;
        this.documentos=documentos;this.detalles=detalles;this.posiciones=posiciones;this.numerador=numerador;
        this.productoService=productoService;this.mallaService=mallaService;
        this.tiposArticulo=tiposArticulo;
    }

    public CatalogoDtos.Configuracion configuracion(Long empId) {
        Empresa empresa=empresa(empId); String token=valor(empId,TOKEN);
        if(token.isBlank()) token=guardarParametro(empId,TOKEN,"Token público del catálogo",nuevoToken(),"SISTEMA").getParVal();
        return new CatalogoDtos.Configuracion(booleano(empId,PUBLICADO),booleano(empId,DOMICILIO),
                token,empresa.getEmpIma()!=null&&!empresa.getEmpIma().isBlank());
    }

    @Transactional
    public CatalogoDtos.Configuracion configurar(Long empId,String usuario,CatalogoDtos.ConfiguracionEntrada entrada){
        if(entrada.publicado()) validarPublicable(empId);
        guardarParametro(empId,PUBLICADO,"Catálogo publicado",String.valueOf(entrada.publicado()),usuario);
        guardarParametro(empId,DOMICILIO,"Permitir pedidos a domicilio",String.valueOf(entrada.permitirDomicilio()),usuario);
        return configuracion(empId);
    }

    public List<CatalogoPosicion> posiciones(Long empId){return posiciones.findByEmpIdOrderByCapFilAscCapColAsc(empId);}
    @Transactional public CatalogoPosicion guardarPosicion(Long empId,String usuario,CatalogoPosicion entrada){
        if(entrada.getCapFil()==null||entrada.getCapFil()<1||entrada.getCapCol()==null||entrada.getCapCol()<1||entrada.getCapUbi()==null||entrada.getCapUbi().isBlank())
            error(HttpStatus.BAD_REQUEST,"Ubicación, fila y columna son obligatorias.");
        CatalogoPosicion p=entrada.getCapId()==null?posiciones.findByEmpIdAndCapFilAndCapCol(empId,entrada.getCapFil(),entrada.getCapCol()).orElse(new CatalogoPosicion()):posiciones.findByEmpIdAndCapId(empId,entrada.getCapId()).orElseThrow();
        p.setEmpId(empId);p.setCapFil(entrada.getCapFil());p.setCapCol(entrada.getCapCol());p.setCapUbi(entrada.getCapUbi().trim());
        if(p.getCapToken()==null)p.setCapToken(nuevoToken());p.setCapAct(true);p.setCapUsuMov(usuario);p.setCapFecMov(LocalDateTime.now());return posiciones.save(p);
    }
    @Transactional public CatalogoPosicion regenerar(Long empId,Long id,String usuario){CatalogoPosicion p=posiciones.findByEmpIdAndCapId(empId,id).orElseThrow();p.setCapToken(nuevoToken());p.setCapUsuMov(usuario);p.setCapFecMov(LocalDateTime.now());return posiciones.save(p);}

    public CatalogoDtos.CatalogoPublico catalogo(String token){
        ContextoPublico c=contexto(token);if(!booleano(c.empId(),PUBLICADO))error(HttpStatus.NOT_FOUND,"El catálogo no está disponible.");Empresa e=empresa(c.empId());
        List<CatalogoDtos.ProductoPublico> lista=new java.util.ArrayList<>();
        productos.findByEmpIdAndProActTrueAndProVisCatTrueOrderByProCatAscProSubCatAscProNomAsc(c.empId()).stream().filter(p->!"B".equals(p.getProTipMov())).map(p->new CatalogoDtos.ProductoPublico("PRODUCTO",p.getProId(),p.getProNom(),p.getProDes(),p.getProTipPro(),p.getProSubCat(),p.getProPreFin(),Boolean.TRUE.equals(p.getProConSto())&&(p.getProStoAct()==null||p.getProStoAct()<=0),"/catalogo/publico/"+token+"/productos/"+p.getProId()+"/imagen")).forEach(lista::add);
        servicios.findByEmpIdAndSerActTrueAndSerVisCatTrueOrderBySerCatAscSerSubCatAscSerNomAsc(c.empId()).stream().filter(s->!"B".equals(s.getSerTipMov())).map(s->new CatalogoDtos.ProductoPublico("SERVICIO",s.getSerId(),s.getSerNom(),s.getSerDes(),s.getSerTipSer(),s.getSerSubCat(),s.getSerPreFin(),false,"/catalogo/publico/"+token+"/servicios/"+s.getSerId()+"/imagen")).forEach(lista::add);
        lista.sort((primero,segundo)->{
            int clase=primero.tipo().equals(segundo.tipo())?0:("PRODUCTO".equals(primero.tipo())?-1:1);
            if(clase!=0)return clase;
            int categoria=java.util.Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER).compare(primero.categoria(),segundo.categoria());
            if(categoria!=0)return categoria;
            return Long.compare(primero.id(),segundo.id());
        });
        return new CatalogoDtos.CatalogoPublico(e.getEmpNom(),"/catalogo/publico/"+token+"/empresa/imagen",booleano(c.empId(),DOMICILIO),c.posicion()==null?"DOMICILIO":"EN_POSICION",c.posicion()==null?null:c.posicion().getCapUbi(),lista);
    }

    @Transactional public CatalogoDtos.PedidoConfirmacion pedir(String token,CatalogoDtos.PedidoEntrada entrada){ContextoPublico c=contexto(token);if(!booleano(c.empId(),PUBLICADO))error(HttpStatus.CONFLICT,"El catálogo no está disponible.");if(c.posicion()!=null&&!"EN_POSICION".equals(entrada.modalidad()))error(HttpStatus.BAD_REQUEST,"Este QR corresponde a un pedido en posición.");if(c.posicion()==null&&(!"DOMICILIO".equals(entrada.modalidad())||!booleano(c.empId(),DOMICILIO)))error(HttpStatus.BAD_REQUEST,"El envío a domicilio no está disponible.");if("DOMICILIO".equals(entrada.modalidad())&&(entrada.direccionEnvio()==null||entrada.direccionEnvio().isBlank()))error(HttpStatus.BAD_REQUEST,"La dirección de envío es obligatoria.");
        Persona persona=persona(c.empId(),entrada.nombre(),entrada.telefono());DocumentoVenta d=new DocumentoVenta();d.setEmpId(c.empId());d.setDovTip("PED");d.setDovNum(numerador.siguiente(c.empId(),"PED"));d.setPerId(persona.getPerId());d.setDovFec(LocalDate.now());d.setDovEst("EMITIDO");d.setDovOri("CATALOGO");d.setDovMod(entrada.modalidad());d.setDovDirEnv(entrada.direccionEnvio());d.setDovObs(entrada.observaciones());d.setDovUbi(c.posicion()==null?entrada.direccionEnvio():c.posicion().getCapUbi());if(c.posicion()!=null){d.setDovFilMal(c.posicion().getCapFil());d.setDovColMal(c.posicion().getCapCol());}d.setDovUsuMov("CATALOGO");d.setDovFecMov(LocalDateTime.now());d.setDovAct(true);d.setDovImpSub(BigDecimal.ZERO);d.setDovImpDes(BigDecimal.ZERO);d.setDovImpIva(BigDecimal.ZERO);d.setDovImpTot(BigDecimal.ZERO);d=documentos.save(d);d.setDovIdRai(d.getDovId());BigDecimal total=BigDecimal.ZERO,subtotal=BigDecimal.ZERO,descuento=BigDecimal.ZERO,iva=BigDecimal.ZERO;
        for(CatalogoDtos.LineaPedido l:entrada.lineas()){
            DocumentoVentaDetalle x=new DocumentoVentaDetalle();BigDecimal base;BigDecimal impDes;BigDecimal impIva;
            if("SERVICIO".equals(l.tipo())){Servicio s=servicios.findByEmpIdAndSerIdAndSerActTrue(c.empId(),l.productoId()).filter(v->Boolean.TRUE.equals(v.getSerVisCat())&&!"B".equals(v.getSerTipMov())).orElseThrow(()->new ResponseStatusException(HttpStatus.CONFLICT,"Servicio no disponible."));base=s.getSerPreVen().multiply(BigDecimal.valueOf(l.cantidad()));impDes=(s.getSerPreDes()==null?BigDecimal.ZERO:s.getSerPreDes()).multiply(BigDecimal.valueOf(l.cantidad()));BigDecimal neto=base.subtract(impDes);impIva=neto.multiply(s.getSerPreIva()).divide(BigDecimal.valueOf(100),4,RoundingMode.HALF_UP);x.setDvdTipLin("S");x.setSerId(s.getSerId());x.setDvdNom(s.getSerNom());x.setDvdPre(s.getSerPreVen());x.setDvdDes(s.getSerPreDes()==null?BigDecimal.ZERO:s.getSerPreDes());x.setDvdIva(s.getSerPreIva());x.setDvdDurUni(s.getSerDurMin()==null?0:s.getSerDurMin());x.setDvdImp(neto.add(impIva).setScale(2,RoundingMode.HALF_UP));}
            else{Producto p=productos.findByEmpIdAndProIdAndProActTrue(c.empId(),l.productoId()).filter(v->Boolean.TRUE.equals(v.getProVisCat())&&!"B".equals(v.getProTipMov())).orElseThrow(()->new ResponseStatusException(HttpStatus.CONFLICT,"Producto no disponible."));productoService.descontarStock(c.empId(),p.getProId(),l.cantidad(),"CATALOGO");base=p.getProPreVen().multiply(BigDecimal.valueOf(l.cantidad()));impDes=base.multiply(p.getProPreDes()).divide(BigDecimal.valueOf(100),4,RoundingMode.HALF_UP);BigDecimal neto=base.subtract(impDes);impIva=neto.multiply(p.getProPreIva()).divide(BigDecimal.valueOf(100),4,RoundingMode.HALF_UP);x.setDvdTipLin("P");x.setProId(p.getProId());x.setDvdNom(p.getProNom());x.setDvdPre(p.getProPreVen());x.setDvdDes(p.getProPreDes());x.setDvdIva(p.getProPreIva());x.setDvdDurUni(p.getProDurMin()==null?0:p.getProDurMin());x.setDvdImp(neto.add(impIva).setScale(2,RoundingMode.HALF_UP));}
            x.setEmpId(c.empId());x.setDovId(d.getDovId());x.setDvdObs(l.observaciones());x.setDvdCan(l.cantidad());x.setDvdDurTot(x.getDvdDurUni()*l.cantidad());x.setDvdUsuMov("CATALOGO");x.setDvdFecMov(LocalDateTime.now());x.setDvdAct(true);detalles.save(x);subtotal=subtotal.add(base);descuento=descuento.add(impDes);iva=iva.add(impIva);total=total.add(x.getDvdImp());
        }
        d.setDovImpSub(subtotal.setScale(2,RoundingMode.HALF_UP));d.setDovImpDes(descuento.setScale(2,RoundingMode.HALF_UP));d.setDovImpIva(iva.setScale(2,RoundingMode.HALF_UP));d.setDovImpTot(total.setScale(2,RoundingMode.HALF_UP));documentos.save(d);if(c.posicion()!=null)mallaService.sincronizarPedido(d);return new CatalogoDtos.PedidoConfirmacion(d.getDovNum(),d.getDovImpTot(),d.getDovMod(),d.getDovUbi());}

    public ContextoPublico contexto(String token){var posicion=posiciones.findByCapTokenAndCapActTrue(token);if(posicion.isPresent())return new ContextoPublico(posicion.get().getEmpId(),posicion.get());Parametro general=parametros.findByParModIgnoreCaseAndParCodIgnoreCaseAndParActTrue(MODULO,TOKEN).stream().filter(p->token.equals(p.getParVal())).findFirst().orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Enlace de catálogo no válido."));return new ContextoPublico(general.getEmpId(),null);}
    public record ContextoPublico(Long empId,CatalogoPosicion posicion){}
    private Persona persona(Long empId,String nombre,String telefono){List<Persona> existentes=personas.buscarCoincidenciasExactas(empId,"",telefono,"");if(!existentes.isEmpty())return existentes.get(0);Persona p=new Persona();p.setEmpId(empId);p.setPerId(personas.obtenerSiguienteId(empId));p.setPerIdHis(1L);p.setPerTipMov("A");p.setPerCauMov("Alta desde catálogo");p.setPerTipPer("F");p.setPerNom(nombre.trim());p.setPerNomCom(nombre.trim());p.setPerTel(telefono.trim());p.setPerAct(true);p.setPerUsuMov("CATALOGO");p.setPerFecMov(LocalDateTime.now());return personas.save(p);}
    private void validarPublicable(Long empId){Empresa e=empresa(empId);if(e.getEmpIma()==null||e.getEmpIma().isBlank())error(HttpStatus.CONFLICT,"La empresa debe tener una imagen.");}
    public Optional<TipoArticulo> tipoArticulo(Long emp,String clase,String nombre){return tiposArticulo.findByEmpIdAndClaseAndNombreIgnoreCaseAndActivoTrue(emp,clase,nombre).filter(t->t.getImagen()!=null&&!t.getImagen().isBlank());}
    private Empresa empresa(Long id){return empresas.findByEmpId(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Empresa no encontrada."));}
    private boolean booleano(Long emp,String cod){return Boolean.parseBoolean(valor(emp,cod));}
    private String valor(Long emp,String cod){return parametros.findByEmpIdAndParModIgnoreCaseAndParCodIgnoreCase(emp,MODULO,cod).filter(p->Boolean.TRUE.equals(p.getParAct())).map(p->p.getParVal()).orElse("");}
    private Parametro guardarParametro(Long emp,String cod,String des,String val,String usu){Parametro p=parametros.findByEmpIdAndParModIgnoreCaseAndParCodIgnoreCase(emp,MODULO,cod).orElse(new Parametro());p.setEmpId(emp);p.setParMod(MODULO);p.setParCod(cod);p.setParDes(des);p.setParVal(val);p.setParAct(true);p.setParUsuMov(usu);p.setParFecMov(LocalDateTime.now());return parametros.save(p);}
    private String nuevoToken(){return UUID.randomUUID().toString().replace("-","");}
    private void error(HttpStatus estado,String mensaje){throw new ResponseStatusException(estado,mensaje);}
}
