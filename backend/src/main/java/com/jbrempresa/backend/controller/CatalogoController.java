package com.jbrempresa.backend.controller;

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.ByteArrayOutputStream;
import java.util.List;
import javax.imageio.ImageIO;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.jbrempresa.backend.core.context.ContextoOperacion;
import com.jbrempresa.backend.dto.ventas.CatalogoDtos;
import com.jbrempresa.backend.entity.CatalogoPosicion;
import com.jbrempresa.backend.entity.Producto;
import com.jbrempresa.backend.repository.EmpresaRepository;
import com.jbrempresa.backend.repository.ProductoRepository;
import com.jbrempresa.backend.repository.ServicioRepository;
import com.jbrempresa.backend.service.CatalogoService;
import com.jbrempresa.backend.service.ImagenService;
import com.jbrempresa.backend.core.config.ConfiguracionRedsysBizumService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/catalogo")
@CrossOrigin(origins="http://localhost:4200")
public class CatalogoController {
    private final CatalogoService catalogo; private final ContextoOperacion contexto;
    private final ImagenService imagenes; private final ProductoRepository productos; private final ServicioRepository servicios; private final EmpresaRepository empresas;
    private final ConfiguracionRedsysBizumService configuracionRedsys;
    public CatalogoController(CatalogoService catalogo,ContextoOperacion contexto,ImagenService imagenes,ProductoRepository productos,ServicioRepository servicios,EmpresaRepository empresas,ConfiguracionRedsysBizumService configuracionRedsys){this.catalogo=catalogo;this.contexto=contexto;this.imagenes=imagenes;this.productos=productos;this.servicios=servicios;this.empresas=empresas;this.configuracionRedsys=configuracionRedsys;}

    @GetMapping("/gestion/configuracion") public CatalogoDtos.Configuracion configuracion(){return catalogo.configuracion(contexto.empresaId());}
    @PutMapping("/gestion/configuracion") public CatalogoDtos.Configuracion configurar(@Valid @RequestBody CatalogoDtos.ConfiguracionEntrada e){return catalogo.configurar(contexto.empresaId(),contexto.nombreUsuario(),e);}
    @GetMapping("/gestion/pago") public ConfiguracionRedsysBizumService.EstadoConfiguracion configuracionPago(){return configuracionRedsys.estado(contexto.empresaId());}
    @GetMapping("/gestion/posiciones") public List<CatalogoPosicion> posiciones(){return catalogo.posiciones(contexto.empresaId());}
    @PostMapping("/gestion/posiciones") public CatalogoPosicion posicion(@RequestBody CatalogoPosicion p){return catalogo.guardarPosicion(contexto.empresaId(),contexto.nombreUsuario(),p);}
    @PostMapping("/gestion/posiciones/{id}/regenerar") public CatalogoPosicion regenerar(@PathVariable Long id){return catalogo.regenerar(contexto.empresaId(),id,contexto.nombreUsuario());}
    @GetMapping(value="/gestion/posiciones/{id}/qr",produces=MediaType.IMAGE_PNG_VALUE)
    public byte[] qr(@PathVariable Long id,@RequestParam String baseUrl)throws Exception{CatalogoPosicion p=catalogo.posiciones(contexto.empresaId()).stream().filter(x->x.getCapId().equals(id)).findFirst().orElseThrow();return generarQr(baseUrl.replaceAll("/+$","")+"/catalogo/"+p.getCapToken(),p);}

    @GetMapping("/publico/{token}") public CatalogoDtos.CatalogoPublico publico(@PathVariable String token){return catalogo.catalogo(token);}
    @PostMapping("/publico/{token}/pedidos") public CatalogoDtos.PedidoConfirmacion pedir(@PathVariable String token,@Valid @RequestBody CatalogoDtos.PedidoEntrada e){return catalogo.pedir(token,e);}
    @GetMapping("/publico/{token}/productos/{id}/imagen") public ResponseEntity<Resource> imagenProducto(@PathVariable String token,@PathVariable Long id){var c=catalogo.contexto(token);Producto p=productos.findByEmpIdAndProIdAndProActTrue(c.empId(),id).orElseThrow();var tipo=catalogo.tipoArticulo(c.empId(),"PRODUCTO",p.getProTipPro()).orElseThrow();return imagen(imagenes.cargar(c.empId(),"PRODUCTOS","tipos",tipo.getImagen()));}
    @GetMapping("/publico/{token}/servicios/{id}/imagen") public ResponseEntity<Resource> imagenServicio(@PathVariable String token,@PathVariable Long id){var c=catalogo.contexto(token);var s=servicios.findByEmpIdAndSerIdAndSerActTrue(c.empId(),id).orElseThrow();var tipo=catalogo.tipoArticulo(c.empId(),"SERVICIO",s.getSerTipSer()).orElseThrow();return imagen(imagenes.cargar(c.empId(),"SERVICIOS","tipos",tipo.getImagen()));}
    @GetMapping("/publico/{token}/empresa/imagen") public ResponseEntity<Resource> imagenEmpresa(@PathVariable String token){var c=catalogo.contexto(token);var e=empresas.findByEmpId(c.empId()).orElseThrow();return imagen(imagenes.cargar(c.empId(),"ADMINISTRACION","empresas",e.getEmpIma()));}

    private byte[] generarQr(String texto,CatalogoPosicion posicion)throws Exception{
        BitMatrix matriz=new QRCodeWriter().encode(texto,BarcodeFormat.QR_CODE,600,600);
        BufferedImage codigo=MatrixToImageWriter.toBufferedImage(matriz);
        BufferedImage lamina=new BufferedImage(760,860,BufferedImage.TYPE_INT_RGB);
        Graphics2D grafico=lamina.createGraphics();
        grafico.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        grafico.setColor(Color.WHITE);grafico.fillRect(0,0,lamina.getWidth(),lamina.getHeight());
        grafico.drawImage(codigo,80,55,null);
        grafico.setColor(new Color(23,32,51));
        dibujarCentrado(grafico,posicion.getCapUbi(),700,36,Font.BOLD);
        dibujarCentrado(grafico,"Fila "+posicion.getCapFil()+"  |  Columna "+posicion.getCapCol(),765,28,Font.PLAIN);
        dibujarCentrado(grafico,"Escanee para abrir el catalogo",815,20,Font.PLAIN);
        grafico.dispose();
        ByteArrayOutputStream out=new ByteArrayOutputStream();ImageIO.write(lamina,"png",out);return out.toByteArray();
    }
    private void dibujarCentrado(Graphics2D grafico,String texto,int y,int tamanio,int estilo){
        String valor=texto==null?"":texto;
        Font fuente=new Font("SansSerif",estilo,tamanio);
        grafico.setFont(fuente);
        while(tamanio>16&&grafico.getFontMetrics().stringWidth(valor)>700){tamanio-=2;fuente=new Font("SansSerif",estilo,tamanio);grafico.setFont(fuente);}
        grafico.drawString(valor,(760-grafico.getFontMetrics().stringWidth(valor))/2,y);
    }
    private ResponseEntity<Resource> imagen(Resource r){String n=r.getFilename()==null?"":r.getFilename().toLowerCase();MediaType t=n.endsWith(".svg")?MediaType.parseMediaType("image/svg+xml"):n.endsWith(".png")?MediaType.IMAGE_PNG:n.endsWith(".webp")?MediaType.parseMediaType("image/webp"):MediaType.IMAGE_JPEG;return ResponseEntity.ok().contentType(t).cacheControl(CacheControl.noCache()).body(r);}
}
