import fs from 'node:fs';
import path from 'node:path';
import { fileURLToPath } from 'node:url';

const root=path.resolve(path.dirname(fileURLToPath(import.meta.url)),'..');
const read=p=>fs.readFileSync(path.join(root,p),'utf8');
const walk=d=>fs.readdirSync(path.join(root,d),{withFileTypes:true}).flatMap(e=>e.isDirectory()?walk(`${d}/${e.name}`):[`${d}/${e.name}`]);
const clean=s=>s.replace(/\|/g,'&#124;').replace(/\s+/g,' ').trim();
const link=(from,to)=>path.relative(path.dirname(from),to).replaceAll('\\','/');
const stamp='Revisión: 2026-09-15. Inventario generado desde las fuentes; no acredita pruebas funcionales.\n';
const generated=new Map();

function argumentsAt(s,start){
  let depth=0,quoted=false,escape=false;
  for(let i=start;i<s.length;i++){
    const c=s[i];
    if(escape){escape=false;continue;}
    if(c==='\\'&&quoted){escape=true;continue;}
    if(c==='"'){quoted=!quoted;continue;}
    if(quoted)continue;
    if(c==='(')depth++;
    if(c===')'&&--depth===0)return {text:s.slice(start+1,i),end:i+1};
  }
  throw Error('Paréntesis sin cerrar');
}

const apiFile='backend/docs/API_INVENTARIO.md';
let api=`# Inventario de endpoints REST\n\n${stamp}\nGenerar: \`node docs/verificar-documentacion.mjs --generar\`. Validar sin escribir: \`node docs/verificar-documentacion.mjs\`.\n\nVer [contrato y permisos](API.md). Cada controlador enlazado define validación, parámetros y DTO. La tabla inventaría métodos declarados, no permisos inferidos por su nombre.\n`;
let endpoints=0;
for(const file of walk('backend/src/main/java/com/jbrempresa/backend/controller').filter(p=>p.endsWith('.java')).sort()){
  const s=read(file); const classAt=s.search(/\bclass\s+/);
  const classHeader=s.slice(0,classAt);
  const base=classHeader.match(/@RequestMapping\s*\(\s*(?:value\s*=\s*|path\s*=\s*)?"([^"]*)"/)?.[1]||'';
  const rows=[];
  const regex=/@(Get|Post|Put|Patch|Delete)Mapping\b/g;
  for(const m of s.matchAll(regex)){
    let end=m.index+m[0].length; while(/\s/.test(s[end]||'')&&end<s.length)end++;
    const args=s[end]==='('?argumentsAt(s,end):{text:'',end};
    const route=args.text.match(/^\s*(?:(?:value|path)\s*=\s*)?"([^"]*)"/)?.[1]||'';
    const method=s.slice(args.end).match(/public\s+([^{};]+?)\s+(\w+)\s*\(/);
    if(!method)throw Error(`No se interpreta método en ${file}`);
    const start=args.end+method.index+method[0].length-1;
    const params=argumentsAt(s,start).text;
    rows.push(`| ${m[1].toUpperCase()} | \`${base+route||'/'}\` | \`${clean(method[1])}\` | \`${clean(method[2]+'('+params+')')}\` |`); endpoints++;
  }
  if(rows.length)api+=`\n## ${path.basename(file,'.java')}\n\n[Fuente](${link(apiFile,file)}).\n\n| Método | Ruta | Salida Java | Operación y parámetros Java |\n|---|---|---|---|\n${rows.join('\n')}\n`;
}
generated.set(apiFile,api);

const modelFile='backend/docs/MODELO_INVENTARIO.md';
let model=`# Inventario de entidades persistidas\n\n${stamp}\nVer [modelo funcional](MODELO_DATOS.md). Generado desde \`@Entity\`, \`@Table\` y campos declarados; las clases ID, asociaciones, restricciones y columnas exactas se consultan en las fuentes enlazadas. No es una introspección de la base local.\n\n| Entidad | Tabla declarada | Campos Java declarados | Fuente |\n|---|---|---|---|\n`;
let entities=0;
for(const file of walk('backend/src/main/java/com/jbrempresa/backend/entity').filter(p=>p.endsWith('.java')).sort()){
  const s=read(file);if(!/@Entity\b/.test(s))continue;
  const table=s.match(/@Table\s*\(\s*name\s*=\s*"([^"]+)"/)?.[1]||'(convención JPA)';
  const fields=[...s.matchAll(/private\s+(?!static\b|final\b)([\w<>?,. \[\]]+)\s+(\w+)\s*(?:[;=])/g)].map(m=>m[2]);
  model+=`| ${path.basename(file,'.java')} | \`${table}\` | ${fields.map(f=>'`'+f+'`').join(', ')} | [Java](${link(modelFile,file)}) |\n`;entities++;
}
generated.set(modelFile,model);

const routesFile='frontend/docs/RUTAS.md';
let routes=`# Inventario de rutas Angular\n\n${stamp}\nFuente: [app.routes.ts](../src/app/app.routes.ts). Las rutas con \`authGuard\` requieren sesión y la guarda aplica el perfil; no sustituyen permisos backend. Ver [guía de uso](GUIA_USO.md) y [seguridad](../../backend/docs/SEGURIDAD.md). El menú efectivo se configura además por empresa.\n\n| Ruta | Destino declarado | Guarda declarada |\n|---|---|---|\n`;
const routeSource=read('frontend/src/app/app.routes.ts');
for(const m of routeSource.matchAll(/path:\s*'([^']*)'/g)){
  // La región termina antes de la siguiente declaración de path.
  const next=routeSource.slice(m.index+5).search(/\bpath:\s*'/);
  const region=routeSource.slice(m.index,next<0?undefined:m.index+5+next);
  const component=region.match(/component:\s*(\w+)/)?.[1];
  const redirect=region.match(/redirectTo:\s*'([^']*)'/)?.[1];
  routes+=`| \`/${m[1]}\` | ${component||('Redirección: '+(redirect??'ver fuente'))} | ${region.includes('canActivate: [authGuard]')?'authGuard':'Sin authGuard declarado'} |\n`;
}
generated.set(routesFile,routes);

const generate=process.argv.includes('--generar');
let failures=[];
for(const [p,text] of generated){
  if(generate)fs.writeFileSync(path.join(root,p),text);
  else if(!fs.existsSync(path.join(root,p))||read(p)!==text)failures.push(`Inventario desactualizado: ${p}`);
}
const dirs=['frontend/docs','backend/docs','docs'];
const docs=dirs.flatMap(walk).filter(p=>p.endsWith('.md'));
for(const file of docs){
  const s=read(file);
  for(const m of s.matchAll(/\[[^\]]*\]\(([^)]+)\)/g)){
    let target=m[1].replace(/^<|>$/g,'').split('#')[0];
    if(!target||/^[a-z]+:/i.test(target))continue;
    try{target=decodeURIComponent(target);}catch{}
    if(!fs.existsSync(path.resolve(root,path.dirname(file),target)))failures.push(`Enlace inexistente: ${file} -> ${target}`);
  }
  if(s.includes('\uFFFD'))failures.push(`Carácter de reemplazo en ${file}`);
}
for(const dir of ['frontend/docs','backend/docs','docs/IA']){
  const index=read(`${dir}/README.md`);
  for(const file of fs.readdirSync(path.join(root,dir)).filter(f=>f.endsWith('.md')&&f!=='README.md'))
    if(!index.includes(`](${file})`))failures.push(`Fuera de índice: ${dir}/${file}`);
}
for(const [dir,prefix] of [['frontend/docs','FE'],['backend/docs','BE']]){
  const ids=[...read(`${dir}/DIRECTIVAS.md`).matchAll(new RegExp(`\\*\\*${prefix}-DIR-(\\d+)`,'g'))].map(m=>+m[1]);
  if(!ids.length||ids.some((x,i)=>x!==i+1))failures.push(`Secuencia incorrecta: ${dir}/DIRECTIVAS.md`);
}
console.log(`${docs.length} Markdown; ${endpoints} endpoints; ${entities} entidades. Enlaces, índices, directivas e inventarios comprobados.`);
if(failures.length){failures.forEach(f=>console.error(f));process.exitCode=1;}
else console.log(generate?'Inventarios generados y comprobación correcta.':'Comprobación correcta.');
