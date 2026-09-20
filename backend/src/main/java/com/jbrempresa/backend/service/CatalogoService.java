package com.jbrempresa.backend.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.text.Normalizer;
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
    public static final String ALIAS = "CATALOGO_ALIAS";
    private final ParametroRepository parametros; private final EmpresaRepository empresas;
    private final ProductoRepository productos; private final ServicioRepository servicios; private final PersonaRepository personas;
    private final DocumentoVentaRepository documentos; private final DocumentoVentaDetalleRepository detalles;
    private final CatalogoPosicionRepository posiciones; private final NumeradorDocumentoVentaService numerador;
    private final ProductoService productoService; private final MallaService mallaService;
    private final TipoArticuloRepository tiposArticulo;
    private final AvisoAlertaService avisosAlertas;
    private final AsignacionPedidoService asignacionPedidos;
    private final FacturacionPedidoService facturacionPedidos;
    private final ProductoComponenteRepository componentesProducto; private final ComponenteRepository componentes;

    public CatalogoService(ParametroRepository parametros, EmpresaRepository empresas, ProductoRepository productos, ServicioRepository servicios,
            PersonaRepository personas, DocumentoVentaRepository documentos, DocumentoVentaDetalleRepository detalles,
            CatalogoPosicionRepository posiciones, NumeradorDocumentoVentaService numerador,
            ProductoService productoService, MallaService mallaService, TipoArticuloRepository tiposArticulo, AvisoAlertaService avisosAlertas,
            AsignacionPedidoService asignacionPedidos, FacturacionPedidoService facturacionPedidos,
            ProductoComponenteRepository componentesProducto, ComponenteRepository componentes) {
        this.parametros=parametros;this.empresas=empresas;this.productos=productos;this.servicios=servicios;this.personas=personas;
        this.documentos=documentos;this.detalles=detalles;this.posiciones=posiciones;this.numerador=numerador;
        this.productoService=productoService;this.mallaService=mallaService;
        this.tiposArticulo=tiposArticulo;
        this.avisosAlertas=avisosAlertas;
        this.asignacionPedidos=asignacionPedidos;
        this.facturacionPedidos=facturacionPedidos;
        this.componentesProducto=componentesProducto;this.componentes=componentes;
    }

    public CatalogoDtos.Configuracion configuracion(Long empId) {
        Empresa empresa=empresa(empId); String token=valor(empId,TOKEN);
        if(token.isBlank()) token=guardarParametro(empId,TOKEN,"Token público del catálogo",nuevoToken(),"SISTEMA").getParVal();
        String alias=valor(empId,ALIAS);
        if(alias.isBlank()) alias=guardarParametro(empId,ALIAS,"Enlace corto del catálogo",aliasDisponible(empresa.getEmpNom(),empId),"SISTEMA").getParVal();
        return new CatalogoDtos.Configuracion(booleano(empId,PUBLICADO),booleano(empId,DOMICILIO),
                token,alias,empresa.getEmpIma()!=null&&!empresa.getEmpIma().isBlank());
    }

    @Transactional
    public CatalogoDtos.Configuracion configurar(Long empId,String usuario,CatalogoDtos.ConfiguracionEntrada entrada){
        if(entrada.publicado()) validarPublicable(empId);
        guardarParametro(empId,PUBLICADO,"Catálogo publicado",String.valueOf(entrada.publicado()),usuario);
        guardarParametro(empId,DOMICILIO,"Permitir pedidos a domicilio",String.valueOf(entrada.permitirDomicilio()),usuario);
        String alias=normalizarAlias(entrada.alias());
        boolean ocupado=parametros.findByParModIgnoreCaseAndParCodIgnoreCaseAndParActTrue(MODULO,ALIAS).stream().anyMatch(p->!p.getEmpId().equals(empId)&&alias.equalsIgnoreCase(p.getParVal()));
        if(ocupado) error(HttpStatus.CONFLICT,"El enlace corto ya está asignado a otra empresa.");
        guardarParametro(empId,ALIAS,"Enlace corto del catálogo",alias,usuario);
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
        return catalogoEmpresa(contexto(token),"/catalogo/publico/"+token,"CATALOGO_CLIENTE");
    }
    public CatalogoDtos.CatalogoPublico catalogoEmpresa(ContextoPublico c,String ruta,String ubicacionAvisos){
        if(!booleano(c.empId(),PUBLICADO))error(HttpStatus.NOT_FOUND,"El catálogo no está disponible.");Empresa e=empresa(c.empId());
        List<CatalogoDtos.ProductoPublico> lista=new java.util.ArrayList<>();
        var relaciones=componentesProducto.findByEmpIdOrderByPrcId(c.empId());
        var componentesActivos=componentes.findByEmpIdAndCmpActTrueOrderByCmpId(c.empId()).stream().collect(java.util.stream.Collectors.toMap(Componente::getCmpId,x->x,(a,b)->a.getCmpIdHis()>b.getCmpIdHis()?a:b));
        productos.findByEmpIdAndProActTrueAndProVisCatTrueOrderByProCatAscProSubCatAscProNomAsc(c.empId()).stream().filter(p->!"B".equals(p.getProTipMov())).map(p->new CatalogoDtos.ProductoPublico("PRODUCTO",p.getProId(),p.getProNom(),p.getProDes(),p.getProTipPro(),p.getProSubCat(),p.getProPreFin(),Boolean.TRUE.equals(p.getProConSto())&&(p.getProStoAct()==null||p.getProStoAct()<=0),Boolean.TRUE.equals(p.getProNov()),Boolean.TRUE.equals(p.getProMejPre()),Boolean.TRUE.equals(p.getProOut()),ruta+"/productos/"+p.getProId()+"/imagen",alergenos(p,relaciones,componentesActivos),componentesProducto(p,relaciones))).forEach(lista::add);
        servicios.findByEmpIdAndSerActTrueAndSerVisCatTrueOrderBySerCatAscSerSubCatAscSerNomAsc(c.empId()).stream().filter(s->!"B".equals(s.getSerTipMov())).map(s->new CatalogoDtos.ProductoPublico("SERVICIO",s.getSerId(),s.getSerNom(),s.getSerDes(),s.getSerTipSer(),s.getSerSubCat(),s.getSerPreFin(),false,Boolean.TRUE.equals(s.getSerNov()),Boolean.TRUE.equals(s.getSerMejPre()),Boolean.TRUE.equals(s.getSerOut()),ruta+"/servicios/"+s.getSerId()+"/imagen",List.of(),List.of())).forEach(lista::add);
        lista.sort((primero,segundo)->{
            int clase=primero.tipo().equals(segundo.tipo())?0:("PRODUCTO".equals(primero.tipo())?-1:1);
            if(clase!=0)return clase;
            int categoria=java.util.Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER).compare(primero.categoria(),segundo.categoria());
            if(categoria!=0)return categoria;
            return Long.compare(primero.id(),segundo.id());
        });
        List<CatalogoDtos.AvisoPublico> avisos=avisosAlertas.visibles(c.empId(),ubicacionAvisos).stream().map(a->new CatalogoDtos.AvisoPublico(a.getAviTipo(),a.getAviTitulo(),a.getAviMensaje(),a.getAviEmisor())).toList();
        List<CatalogoDtos.ComponentePublico> componentesCatalogo=componentesActivos.values().stream().sorted(java.util.Comparator.comparing(Componente::getCmpNom,String.CASE_INSENSITIVE_ORDER)).map(x->new CatalogoDtos.ComponentePublico(x.getCmpId(),x.getCmpNom(),precioAdicional(x),alergenos(x))).toList();
        return new CatalogoDtos.CatalogoPublico(e.getEmpNom(),ruta+"/empresa/imagen",booleano(c.empId(),DOMICILIO),c.posicion()==null?"DOMICILIO":"EN_POSICION",c.posicion()==null?null:c.posicion().getCapUbi(),avisos,lista,componentesCatalogo);
    }

    private List<Long> componentesProducto(Producto producto,List<ProductoComponente> relaciones){return relaciones.stream().filter(r->r.getProId().equals(producto.getProId())&&r.getProIdHis().equals(producto.getProIdHis())).map(ProductoComponente::getCmpId).distinct().toList();}

    private List<String> alergenos(Producto producto,List<ProductoComponente> relaciones,java.util.Map<Long,Componente> componentesActivos){
        java.util.Set<String> resultado=new java.util.LinkedHashSet<>();
        relaciones.stream().filter(r->r.getProId().equals(producto.getProId())&&r.getProIdHis().equals(producto.getProIdHis())).map(r->componentesActivos.get(r.getCmpId())).filter(java.util.Objects::nonNull).forEach(c->{
            if(Boolean.TRUE.equals(c.getCmpGluten()))resultado.add("Gluten");if(Boolean.TRUE.equals(c.getCmpCrustaceos()))resultado.add("Crustáceos");if(Boolean.TRUE.equals(c.getCmpHuevos()))resultado.add("Huevos");if(Boolean.TRUE.equals(c.getCmpPescado()))resultado.add("Pescado");if(Boolean.TRUE.equals(c.getCmpCacahuetes()))resultado.add("Cacahuetes");if(Boolean.TRUE.equals(c.getCmpSoja()))resultado.add("Soja");if(Boolean.TRUE.equals(c.getCmpLeche()))resultado.add("Leche");if(Boolean.TRUE.equals(c.getCmpFrutosCascara()))resultado.add("Frutos de cáscara");if(Boolean.TRUE.equals(c.getCmpApio()))resultado.add("Apio");if(Boolean.TRUE.equals(c.getCmpMostaza()))resultado.add("Mostaza");if(Boolean.TRUE.equals(c.getCmpSesamo()))resultado.add("Sésamo");if(Boolean.TRUE.equals(c.getCmpSulfitos()))resultado.add("Sulfitos");if(Boolean.TRUE.equals(c.getCmpAltramuces()))resultado.add("Altramuces");if(Boolean.TRUE.equals(c.getCmpMoluscos()))resultado.add("Moluscos");
        });return List.copyOf(resultado);
    }
    private List<String> alergenos(Componente c){java.util.List<String> r=new java.util.ArrayList<>();if(Boolean.TRUE.equals(c.getCmpGluten()))r.add("Gluten");if(Boolean.TRUE.equals(c.getCmpCrustaceos()))r.add("Crustáceos");if(Boolean.TRUE.equals(c.getCmpHuevos()))r.add("Huevos");if(Boolean.TRUE.equals(c.getCmpPescado()))r.add("Pescado");if(Boolean.TRUE.equals(c.getCmpCacahuetes()))r.add("Cacahuetes");if(Boolean.TRUE.equals(c.getCmpSoja()))r.add("Soja");if(Boolean.TRUE.equals(c.getCmpLeche()))r.add("Leche");if(Boolean.TRUE.equals(c.getCmpFrutosCascara()))r.add("Frutos de cáscara");if(Boolean.TRUE.equals(c.getCmpApio()))r.add("Apio");if(Boolean.TRUE.equals(c.getCmpMostaza()))r.add("Mostaza");if(Boolean.TRUE.equals(c.getCmpSesamo()))r.add("Sésamo");if(Boolean.TRUE.equals(c.getCmpSulfitos()))r.add("Sulfitos");if(Boolean.TRUE.equals(c.getCmpAltramuces()))r.add("Altramuces");if(Boolean.TRUE.equals(c.getCmpMoluscos()))r.add("Moluscos");return r;}
    private BigDecimal precioAdicional(Componente c){BigDecimal base=Optional.ofNullable(c.getCmpPreAdi()).orElse(BigDecimal.ZERO),iva=Optional.ofNullable(c.getCmpIva()).orElse(BigDecimal.ZERO);return base.multiply(BigDecimal.ONE.add(iva.divide(BigDecimal.valueOf(100),6,RoundingMode.HALF_UP))).setScale(2,RoundingMode.HALF_UP);}

    @Transactional public CatalogoDtos.PedidoConfirmacion pedir(String token,CatalogoDtos.PedidoEntrada entrada){return pedirEmpresa(contexto(token),entrada);}
    @Transactional public CatalogoDtos.PedidoConfirmacion pedirEmpresa(ContextoPublico c,CatalogoDtos.PedidoEntrada entrada){DocumentoVenta d=pedirDocumento(c,entrada);return new CatalogoDtos.PedidoConfirmacion(d.getDovNum(),d.getDovImpTot(),d.getDovMod(),d.getDovUbi());}
    @Transactional public DocumentoVenta pedirDocumento(ContextoPublico c,CatalogoDtos.PedidoEntrada entrada){if(!booleano(c.empId(),PUBLICADO))error(HttpStatus.CONFLICT,"El catálogo no está disponible.");if(c.posicion()!=null&&!"EN_POSICION".equals(entrada.modalidad()))error(HttpStatus.BAD_REQUEST,"Este QR corresponde a un pedido en posición.");if(c.posicion()==null&&(!"DOMICILIO".equals(entrada.modalidad())||!booleano(c.empId(),DOMICILIO)))error(HttpStatus.BAD_REQUEST,"El envío a domicilio no está disponible.");if("DOMICILIO".equals(entrada.modalidad())&&(entrada.direccionEnvio()==null||entrada.direccionEnvio().isBlank()))error(HttpStatus.BAD_REQUEST,"La dirección de envío es obligatoria.");
        Persona persona=persona(c.empId(),entrada.nombre(),entrada.telefono(),entrada.correo());DocumentoVenta d=new DocumentoVenta();d.setEmpId(c.empId());d.setDovTip("PED");d.setDovNum(numerador.siguiente(c.empId(),"PED"));d.setPerId(persona.getPerId());d.setDovFec(LocalDate.now());d.setDovEst("EMITIDO");d.setDovPag(false);d.setDovOri("CATALOGO");d.setDovMod(entrada.modalidad());d.setDovDirEnv(entrada.direccionEnvio());d.setDovObs(entrada.observaciones());d.setDovUbi(c.posicion()==null?entrada.direccionEnvio():c.posicion().getCapUbi());if(c.posicion()!=null){d.setDovFilMal(c.posicion().getCapFil());d.setDovColMal(c.posicion().getCapCol());}d.setDovUsuMov("CATALOGO");d.setDovFecMov(LocalDateTime.now());d.setDovAct(true);d.setDovImpSub(BigDecimal.ZERO);d.setDovImpDes(BigDecimal.ZERO);d.setDovImpIva(BigDecimal.ZERO);d.setDovImpTot(BigDecimal.ZERO);d=documentos.save(d);d.setDovIdRai(d.getDovId());BigDecimal total=BigDecimal.ZERO,subtotal=BigDecimal.ZERO,descuento=BigDecimal.ZERO,iva=BigDecimal.ZERO;
        for(CatalogoDtos.LineaPedido l:entrada.lineas()){
            DocumentoVentaDetalle x=new DocumentoVentaDetalle();BigDecimal base;BigDecimal impDes;BigDecimal impIva;String observacionesLinea=l.observaciones();
            if("SERVICIO".equals(l.tipo())){Servicio s=servicios.findByEmpIdAndSerIdAndSerActTrue(c.empId(),l.productoId()).filter(v->Boolean.TRUE.equals(v.getSerVisCat())&&!"B".equals(v.getSerTipMov())).orElseThrow(()->new ResponseStatusException(HttpStatus.CONFLICT,"Servicio no disponible."));ImportesCatalogo importes=importesCatalogo(s.getSerPreFin(),s.getSerPreIva(),l.cantidad());base=importes.base();impDes=BigDecimal.ZERO;impIva=importes.iva();x.setDvdTipLin("S");x.setSerId(s.getSerId());x.setDvdNom(s.getSerNom());x.setDvdPre(importes.precioNetoUnitario());x.setDvdDes(BigDecimal.ZERO);x.setDvdIva(s.getSerPreIva());x.setDvdDurUni(s.getSerDurMin()==null?0:s.getSerDurMin());x.setDvdImp(importes.total());}
            else{Producto p=productos.findByEmpIdAndProIdAndProActTrue(c.empId(),l.productoId()).filter(v->Boolean.TRUE.equals(v.getProVisCat())&&!"B".equals(v.getProTipMov())).orElseThrow(()->new ResponseStatusException(HttpStatus.CONFLICT,"Producto no disponible."));productoService.descontarStock(c.empId(),p.getProId(),l.cantidad(),"CATALOGO");ImportesCatalogo importes=importesCatalogo(p.getProPreFin(),p.getProPreIva(),l.cantidad());Personalizacion personalizacion=personalizar(c.empId(),p,l);base=importes.base().add(personalizacion.base());impDes=BigDecimal.ZERO;impIva=importes.iva().add(personalizacion.iva());BigDecimal importe=base.add(impIva).setScale(2,RoundingMode.HALF_UP);observacionesLinea=personalizacion.observaciones();x.setDvdTipLin("P");x.setProId(p.getProId());x.setDvdNom(p.getProNom());x.setDvdPre(base.divide(BigDecimal.valueOf(l.cantidad()),6,RoundingMode.HALF_UP));x.setDvdDes(BigDecimal.ZERO);x.setDvdIva(base.signum()==0?BigDecimal.ZERO:impIva.multiply(BigDecimal.valueOf(100)).divide(base,4,RoundingMode.HALF_UP));x.setDvdDurUni(p.getProDurMin()==null?0:p.getProDurMin());x.setDvdImp(importe);}
            x.setEmpId(c.empId());x.setDovId(d.getDovId());x.setDvdObs(observacionesLinea);x.setDvdCan(l.cantidad());x.setDvdDurTot(x.getDvdDurUni()*l.cantidad());x.setDvdUsuMov("CATALOGO");x.setDvdFecMov(LocalDateTime.now());x.setDvdAct(true);detalles.save(x);subtotal=subtotal.add(base);descuento=descuento.add(impDes);iva=iva.add(impIva);total=total.add(x.getDvdImp());
        }
        d.setDovImpSub(subtotal.setScale(2,RoundingMode.HALF_UP));d.setDovImpDes(descuento.setScale(2,RoundingMode.HALF_UP));d.setDovImpIva(iva.setScale(2,RoundingMode.HALF_UP));d.setDovImpTot(total.setScale(2,RoundingMode.HALF_UP));documentos.save(d);List<DocumentoVentaDetalle> lineasPedido=detalles.findByEmpIdAndDovIdOrderByDvdId(c.empId(),d.getDovId());asignacionPedidos.asignar(d,lineasPedido);facturacionPedidos.generar(d,lineasPedido,"CATALOGO");if(c.posicion()!=null)mallaService.sincronizarPedido(d);d.setDetalles(lineasPedido);return d;}

    private Personalizacion personalizar(Long empId,Producto producto,CatalogoDtos.LineaPedido linea){
        List<ProductoComponente> relaciones=componentesProducto.findByEmpIdAndProIdAndProIdHisOrderByPrcId(empId,producto.getProId(),producto.getProIdHis());
        java.util.Set<Long> incluidos=relaciones.stream().map(ProductoComponente::getCmpId).collect(java.util.stream.Collectors.toCollection(java.util.LinkedHashSet::new));
        java.util.Set<Long> seleccion=linea.componentes()==null?new java.util.LinkedHashSet<>(incluidos):new java.util.LinkedHashSet<>(linea.componentes());
        if(linea.componentes()!=null&&seleccion.size()!=linea.componentes().size())error(HttpStatus.BAD_REQUEST,"No repita componentes en la personalización.");
        java.util.Map<Long,Componente> activos=componentes.findByEmpIdAndCmpActTrueOrderByCmpId(empId).stream().collect(java.util.stream.Collectors.toMap(Componente::getCmpId,x->x,(a,b)->a.getCmpIdHis()>b.getCmpIdHis()?a:b));
        if(!activos.keySet().containsAll(seleccion))error(HttpStatus.BAD_REQUEST,"La personalización contiene componentes no disponibles para la empresa.");
        List<Componente> anadidos=seleccion.stream().filter(id->!incluidos.contains(id)).map(activos::get).toList();
        List<Componente> eliminados=incluidos.stream().filter(id->!seleccion.contains(id)).map(activos::get).filter(java.util.Objects::nonNull).toList();
        BigDecimal base=anadidos.stream().map(c->Optional.ofNullable(c.getCmpPreAdi()).orElse(BigDecimal.ZERO)).reduce(BigDecimal.ZERO,BigDecimal::add).multiply(BigDecimal.valueOf(linea.cantidad()));
        BigDecimal iva=anadidos.stream().map(c->{BigDecimal precio=Optional.ofNullable(c.getCmpPreAdi()).orElse(BigDecimal.ZERO),tipo=Optional.ofNullable(c.getCmpIva()).orElse(BigDecimal.ZERO);return precio.multiply(tipo).divide(BigDecimal.valueOf(100),6,RoundingMode.HALF_UP);}).reduce(BigDecimal.ZERO,BigDecimal::add).multiply(BigDecimal.valueOf(linea.cantidad()));
        String obs=Optional.ofNullable(linea.observaciones()).orElse("").trim();if(!eliminados.isEmpty())obs=anexarSiFalta(obs,"Sin: "+eliminados.stream().map(Componente::getCmpNom).collect(java.util.stream.Collectors.joining(", ")));if(!anadidos.isEmpty())obs=anexarSiFalta(obs,"Añadir: "+anadidos.stream().map(Componente::getCmpNom).collect(java.util.stream.Collectors.joining(", ")));if(obs.length()>2000)error(HttpStatus.BAD_REQUEST,"La personalización del producto es demasiado extensa.");
        return new Personalizacion(base,iva,obs);
    }
    private String anexar(String actual,String texto){return actual.isBlank()?texto:actual+" | "+texto;}
    private String anexarSiFalta(String actual,String texto){return actual.contains(texto)?actual:anexar(actual,texto);}
    private record Personalizacion(BigDecimal base,BigDecimal iva,String observaciones){}

    public ContextoPublico contexto(String token){var posicion=posiciones.findByCapTokenAndCapActTrue(token);if(posicion.isPresent())return new ContextoPublico(posicion.get().getEmpId(),posicion.get());Optional<Parametro> general=parametros.findByParModIgnoreCaseAndParCodIgnoreCaseAndParActTrue(MODULO,TOKEN).stream().filter(p->token.equals(p.getParVal())).findFirst();if(general.isEmpty())general=parametros.findByParModIgnoreCaseAndParCodIgnoreCaseAndParActTrue(MODULO,ALIAS).stream().filter(p->token.equalsIgnoreCase(p.getParVal())).findFirst();Parametro acceso=general.orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Enlace de catálogo no válido."));return new ContextoPublico(acceso.getEmpId(),null);}
    public record ContextoPublico(Long empId,CatalogoPosicion posicion){}
    private Persona persona(Long empId,String nombre,String telefono,String correo){String tel=telefono==null?"":telefono.trim();String ema=correo==null?"":correo.trim();List<Persona> existentes=personas.buscarCoincidenciasExactas(empId,"",tel,ema);Optional<Persona> coincidente=existentes.stream().filter(p->(tel.isBlank()||normalizarTelefono(tel).equals(normalizarTelefono(p.getPerTel())))&&(ema.isBlank()||ema.equalsIgnoreCase(p.getPerEma()==null?"":p.getPerEma().trim()))).findFirst();if(coincidente.isPresent())return coincidente.get();Persona p=new Persona();p.setEmpId(empId);p.setPerId(personas.obtenerSiguienteId(empId));p.setPerIdHis(1L);p.setPerTipMov("A");p.setPerCauMov("Alta incompleta desde catálogo");p.setPerTipPer("FISICA");p.setPerNom(nombre.trim());p.setPerNomCom(nombre.trim());p.setPerTel(tel);p.setPerEma(ema);p.setPerDatCom(false);p.setPerAct(true);p.setPerUsuMov("CATALOGO");p.setPerFecMov(LocalDateTime.now());return personas.save(p);}
    private String normalizarTelefono(String valor){return valor==null?"":valor.replaceAll("[^0-9]","");}
    private ImportesCatalogo importesCatalogo(BigDecimal precioFinalUnitario,BigDecimal tipoIva,int cantidad){
        BigDecimal finalUnitario=Optional.ofNullable(precioFinalUnitario).orElse(BigDecimal.ZERO).setScale(2,RoundingMode.HALF_UP);
        BigDecimal iva=Optional.ofNullable(tipoIva).orElse(BigDecimal.ZERO);
        BigDecimal total=finalUnitario.multiply(BigDecimal.valueOf(cantidad)).setScale(2,RoundingMode.HALF_UP);
        BigDecimal divisor=BigDecimal.ONE.add(iva.divide(BigDecimal.valueOf(100),10,RoundingMode.HALF_UP));
        BigDecimal base=total.divide(divisor,4,RoundingMode.HALF_UP);
        BigDecimal precioNetoUnitario=base.divide(BigDecimal.valueOf(cantidad),6,RoundingMode.HALF_UP);
        return new ImportesCatalogo(precioNetoUnitario,base,total.subtract(base),total);
    }
    private record ImportesCatalogo(BigDecimal precioNetoUnitario,BigDecimal base,BigDecimal iva,BigDecimal total){}
    private void validarPublicable(Long empId){Empresa e=empresa(empId);if(e.getEmpIma()==null||e.getEmpIma().isBlank())error(HttpStatus.CONFLICT,"La empresa debe tener una imagen.");}
    public Optional<TipoArticulo> tipoArticulo(Long emp,String clase,String nombre){return tiposArticulo.findByEmpIdAndClaseAndNombreIgnoreCaseAndActivoTrue(emp,clase,nombre).filter(t->t.getImagen()!=null&&!t.getImagen().isBlank());}
    private Empresa empresa(Long id){return empresas.findByEmpId(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Empresa no encontrada."));}
    private boolean booleano(Long emp,String cod){return Boolean.parseBoolean(valor(emp,cod));}
    private String valor(Long emp,String cod){return parametros.findByEmpIdAndParModIgnoreCaseAndParCodIgnoreCase(emp,MODULO,cod).filter(p->Boolean.TRUE.equals(p.getParAct())).map(p->p.getParVal()).orElse("");}
    private Parametro guardarParametro(Long emp,String cod,String des,String val,String usu){Parametro p=parametros.findByEmpIdAndParModIgnoreCaseAndParCodIgnoreCase(emp,MODULO,cod).orElse(new Parametro());p.setEmpId(emp);p.setParMod(MODULO);p.setParCod(cod);p.setParDes(des);p.setParVal(val);p.setParAct(true);p.setParUsuMov(usu);p.setParFecMov(LocalDateTime.now());return parametros.save(p);}
    private String nuevoToken(){return UUID.randomUUID().toString().replace("-","");}
    private String normalizarAlias(String texto){String valor=Normalizer.normalize(texto==null?"":texto,Normalizer.Form.NFD).replaceAll("\\p{M}","").toLowerCase().replaceAll("[^a-z0-9]+","-").replaceAll("(^-|-$)","");if(valor.isBlank()||valor.length()>80)error(HttpStatus.BAD_REQUEST,"El enlace corto no es válido.");return valor;}
    private String aliasDisponible(String nombre,Long empId){String base=normalizarAlias(nombre),candidato=base;int sufijo=2;List<Parametro> existentes=parametros.findByParModIgnoreCaseAndParCodIgnoreCaseAndParActTrue(MODULO,ALIAS);while(aliasOcupado(existentes,candidato,empId))candidato=base+"-"+sufijo++;return candidato;}
    private boolean aliasOcupado(List<Parametro> existentes,String alias,Long empId){return existentes.stream().anyMatch(p->!p.getEmpId().equals(empId)&&alias.equalsIgnoreCase(p.getParVal()));}
    private void error(HttpStatus estado,String mensaje){throw new ResponseStatusException(estado,mensaje);}
}
