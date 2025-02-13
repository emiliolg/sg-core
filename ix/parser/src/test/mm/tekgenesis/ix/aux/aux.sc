/*     aux     15/09/14   11:27     */

schema aux descr "Esquema de stock de GARBARINO";

table cajeros descr "Cajeros"
{
	sucursal	num(3)	descr "Sucursal"
					not null
					,
	cajero	num(5)	descr "Codigo del cajero (id)"
					not null
					,
	descrip	char(20)	descr "Logname del Cajero"
					not null
					,
	categ	num(2)	descr "Jerarquía de Operación"
					not null
					default (0)
					,
	clave	char(5)	descr "Clave Personal"
					mask "5#x"
					,
}
primary key	(sucursal, cajero);
grant all on cajeros to public with grant option;

table movcaja descr "Movimientos de Caja"
{
	tipomov	char(1)	descr "Tipo de Movimiento"
					not null
					in ("I":"Inicio de Caja", "C":"Cierre de Caja", "M":"Mov. Diarios de Caja"),
	sucursal	num(3)	descr "Sucursal"
					not null
					,
	cajero	num(5)	descr "Cajero"
					not null
					in cajeros(sucursal):descrip,
	fecha	date	descr "Fecha"
					not null
					default (today)
					,
	nrocomp	num(9)	descr "Numero de Comprobante"
					,
	imputacion	num(2)	descr "Imputacion"
					not null
					,
	concepto	num(2)	descr "Concepto"
					,
	hora	time	descr "Hora"
					not null
					default (hour)
					,
	monto	num(11,2)	descr "Monto"
					not null
					,
	refer	num(11)	descr "Vale Referencia"
					,
	tipcot	num(2)	descr "Moneda de Cotizacion segun Plan de Cuentas"
					,
	mctemonto	num(11,2)	descr "Monto en Moneda Corriente"
					,
}
primary key	(sucursal, fecha, cajero, tipomov, nrocomp, concepto, imputacion) [ 56 ],
index moneda	(sucursal, fecha, cajero, tipcot, concepto, imputacion, tipomov, nrocomp) [ 56 ];
grant all on movcaja to public with grant option;

table vtatel descr "Ventas telefonicas"
{
	nroped	num(8)	descr "Número de pedido"
					not null
					,
	renglon	num(2)	descr "Renglon del pedido"
					not null
					,
	codadm	char(8)	descr "Codigo de administración"
					,
	cant	num(9)	descr "Cantidad"
					,
	precio	num(11,2)	descr "Precio"
					,
	fecha	date	descr "Fecha del pedido"
					,
	cumplido	char(1)	descr "Cumplido"
					mask "1>A"
					in ("S":"Cumplido", "N":"No cumplido"),
	nvta	num(8)	descr "Nota de Venta Relacionada"
					,
}
primary key	(nroped, renglon),
index codadm	(codadm, fecha, nroped, renglon);
grant all on vtatel to public with grant option;

table cotiza descr "Cotizacion Diaria"
{
	codmon	num(2)	descr "Código de Moneda"
					not null
					in moneda:descrip,
	fecha	date	descr "Fecha de Validez de la Cotización Desde"
					not null
					,
	hora	time	descr "Hora de Validez de la Cotización Desde"
					not null
					,
	valor	float	descr "Valor de La Cotización"
					not null
					,
	cdate	date	descr "Fecha de Carga"
					,
	ctime	time	descr "Hora de Carga"
					,
	cuid	num(5)	descr "Usuario que Cargo"
					,
}
primary key	(codmon, fecha, hora);
grant all on cotiza to public with grant option;

table tivaf descr "Tipos de IVA "
{
	tipo	num(2)	descr "Número del tipo de IVA"
					not null
					,
	fdesde	date	descr "Fecha de vigencia"
					not null
					default (today)
					,
	porcent	num(5,2)	descr "Porcentaje de IVA"
					not null
					default (0.00)
					,
	porcent2	num(5,2)	descr "Porcentaje 2 de IVA"
					not null
					default (0.00)
					,
	porc3337	num(5,2)	descr "Porcentaje IVA R3337"
					not null
					default (0.00)
					,
}
primary key	(tipo, fdesde),
unique index tivafec	(fdesde, tipo);
grant all on tivaf to public with grant option;

table grupos descr "Grupos Diversos"
{
	tipgrup	char(1)	descr "Tipo de Grupo"
					mask ">A"
					in ("S":"Sucursales", "E":"Estados del STOCK", "M":"Estados del MOVIM", "O":"Oper. sobre arch.", "T":"Tipos de Mov.", "W":"Estados del Workflow", "G":"Grupos"),
	nrogrup	num(4)	descr "Número de Grupo"
					not null
					,
	descrip	char(40)	descr "Descripción"
					not null
					,
	descor	char(5)	descr "Descripción para Columnas"
					not null
					,
	elem	num(3)	descr "Cantidad de Componentes del Grupo"
					,
	tgrpelem	char(1)	descr "Tipo de Grupo de los elementos"
					mask ">A"
					in ("S":"Sucursales", "E":"Estados del STOCK", "M":"Estados del MOVIM", "O":"Oper. sobre arch.", "T":"Tipos de Mov."),
}
primary key	(tipgrup, nrogrup),
unique index descor	(tipgrup, descor),
unique index tgrpelem	(tgrpelem not null, nrogrup);
grant all on grupos to public with grant option;

table rengrup descr "Renglones de Grupos"
{
	tipgrup	char(1)	descr "Tipo de Grupo"
					mask ">A"
					in ("S":"Sucursales", "E":"Estados del STOCK", "M":"Estados del MOVIM", "O":"Oper. sobre arch.", "T":"Tipos de Mov.", "G":"Grupos"),
	nrogrup	num(4)	descr "Número de Grupo"
					not null
					in grupos(tipgrup):descrip,
	orden	num(3)	descr "Orden dentro del Grupo"
					not null
					
					check (this >= 0),
	codigo	num(4)	descr "Código"
					not null
					,
}
primary key	(tipgrup, nrogrup, orden),
unique index codigo	(tipgrup, nrogrup, codigo),
unique index grupos	(codigo, tipgrup, nrogrup);
grant all on rengrup to public with grant option;

table formsal descr "Formatos de Salida"
{
	formato	num(4)	descr "Formato de Salida"
					not null
					,
	descrip	char(60)	descr "Descripción"
					not null
					,
}
primary key	(formato);
grant all on formsal to public with grant option;

table formmod descr "Formatos Asociados a Módulos o Programa"
{
	formato	num(4)	descr "Formato de Salida"
					not null
					in formsal:descrip,
	modulo	char(12)	descr "Módulo - Programa"
					not null
					in progar:descrip,
}
primary key	(formato, modulo),
unique index modulo	(modulo, formato);
grant all on formmod to public with grant option;

table formgrup descr "Grupos Asociados a Formatos de Salida"
{
	formato	num(4)	descr "Formato de Salida"
					not null
					in formsal:descrip,
	tipgrup	char(1)	descr "Tipo de Grupo"
					mask ">A"
					in ("S":"Sucursales", "E":"Estados del STOCK", "M":"Estados del MOVIM", "O":"Oper. sobre arch.", "T":"Tipos de Mov.", "G":"Grupos"),
	nrogrup	num(4)	descr "Número de Grupo"
					not null
					in grupos(tipgrup):descrip,
}
primary key	(formato, tipgrup, nrogrup),
index grupo	(tipgrup, nrogrup, formato);
grant all on formgrup to public with grant option;

table progar descr "Programas de Garbarino"
{
	nomprog	char(12)	descr "Nombre del Programa"
					not null
					,
	descrip	char(60)	descr "Descripcion Funcional"
					not null
					,
	tipprog	num(2)	descr "Tipo de Programa"
					not null
					,
	cantcol	num(2)	descr "Cantidad de Columnas Iterativas x Sucursal"
					,
	grupdef	num(4)	descr "Grupo de Sucursales Default"
					in grupos by tgrpelem("S"):descrip,
	gestdef	num(4)	descr "Grupo de Estados Default"
					in grupos by tgrpelem("E"):descrip,
}
primary key	(nomprog),
unique index tipprog	(tipprog, nomprog),
index grupdef	(grupdef not null),
index gestdef	(gestdef not null);
grant all on progar to public with grant option;

table grupdef descr "Grupos Default de Programas"
{
	nomprog	char(12)	descr "Nombre del Programa"
					not null
					in progar:descrip,
	tgrpelem	char(1)	descr "Tipo de Grupo de los elementos"
					mask ">A"
					in ("S":"Sucursales", "E":"Estados del STOCK", "M":"Estados del MOVIM", "O":"Oper. sobre arch.", "T":"Tipos de Mov."),
	grupdef	num(4)	descr "Grupo de Sucursales Default"
					not null
					in grupos by tgrpelem(tgrpelem):descrip,
}
primary key	(nomprog, tgrpelem),
index grupdef	(grupdef);
grant all on grupdef to public with grant option;

table legcaj descr "Legajos de Cajeros"
{
	fecha	date	descr "Fecha"
					not null
					default (today)
					,
	nroleg	num(9)	descr "Nro. de Legajo"
					not null
					,
	cajero	num(5)	descr "Cajero"
					not null
					in cajeros(sucursal):descrip,
	sucursal	num(3)	descr "Sucursal"
					not null
					,
	emp	num(3)	descr "Empresa"
					,
	empc	num(3)	descr "Empresa de cierre"
					,
	nrolegc	num(9)	descr "Nro. de Legajo de cierre"
					,
	cdate	date	descr "Fecha de Carga"
					,
	ctime	time	descr "Hora de Carga"
					,
	cuid	num(5)	descr "Usuario que Cargo"
					,
}
primary key	(sucursal, fecha, cajero) [ 17 ];
grant all on legcaj to public with grant option;

table ctaold descr "Tabla temporaria de Cuentas a Depurar"
{
	codold	num(8)	descr "Codigo de Cuenta Viejo"
					not null
					,
	codnew	num(8)	descr "Codigo de Cuenta Nuevo"
					not null
					,
	apyold	char(50)	descr "Apellido y Nombre Viejo"
					,
	apynew	char(50)	descr "Apellido y Nombre Nuevo"
					,
}
primary key	(codold);
grant all on ctaold to public with grant option;

table ctanew descr "Tabla definitiva de Cuentas a Depurar"
{
	codold	num(8)	descr "Codigo de Cuenta Viejo"
					not null
					,
	codnew	num(8)	descr "Codigo de Cuenta Nuevo"
					not null
					,
	apyold	char(50)	descr "Apellido y Nombre Viejo"
					,
	apynew	char(50)	descr "Apellido y Nombre Nuevo"
					,
	tipo	num(2)	descr "Tipo de Depuracion"
					not null
					default (1)
					in (1:"Automatica", 2:"Manual"),
	estado	num(2)	descr "Estado de Cuenta"
					not null
					default (1)
					,
	fecha	date	descr "Fecha de Depuracion"
					not null
					default (today)
					,
	hora	time	descr "Hora"
					not null
					default (hour)
					,
	cuid	num(5)	descr "Usuario que Cargo"
					,
}
primary key	(codold),
index estado	(estado, codold);
grant all on ctanew to public with grant option;

table reldir descr "Relacion Entre Dir. Alt."
{
	codold	num(8)	descr "Codigo de Cuenta Viejo"
					not null
					,
	dirold	num(4)	descr "Código de Dirección Viejo"
					not null
					,
	asocia	bool	descr "Asocia una Dir. Alt. en Nueva"
					not null
					,
	codnew	num(8)	descr "Codigo de Cuenta Nuevo"
					,
	dirnew	num(4)	descr "Código de Dirección Nuevo"
					,
	cdate	date	descr "Fecha de Carga"
					,
	ctime	time	descr "Hora de Carga"
					,
	cuid	num(5)	descr "Usuario que Cargo"
					,
	mdate	date	descr "Fecha de Modificacion"
					,
	mtime	time	descr "Hora de Modificacion"
					,
	muid	num(5)	descr "Usuario que Modifico"
					,
}
primary key	(codold, dirold);
grant all on reldir to public with grant option;

table sorteos descr "Definicion de Sorteos"
{
	emp	num(2)	descr "Codigo de Empresa"
					not null
					,
	nrosort	num(4)	descr "Numero de Sorteo"
					not null
					,
	descrip	char(30)	descr "Descripcion"
					,
	descor	char(15)	descr "Descripcion corta"
					,
	fdesde	date	descr "Fecha de vigencia desde"
					,
	fhasta	date	descr "Fecha de vigencia hasta"
					,
	ultfimp	date	descr "Fecha de ultima impresion de cupones"
					,
	ulthimp	time	descr "Hora de ultima impresion de cupones"
					,
	ctrlnc	bool	descr "Controla las anulaciones"
					default (0)
					,
	via	num(2)	descr "Via de Comunicacion"
					,
	hdesde	time	descr "Hora de vigencia desde"
					,
	hhasta	time	descr "Hora de vigencia hasta"
					,
	tpropia	bool	descr "Participa solo Tarjeta Propia"
					,
	ddirecto	bool	descr "Participa solo Debito Directo"
					,
	lregalos	bool	descr "Participan solo L.de Regalo, NO las vtas."
					,
	codflia	char(8)	descr "Codigo de familia a filtrar"
					,
	marca	num(8)	descr "Codigo de marca a filtrar"
					,
}
primary key	(emp, nrosort);
grant all on sorteos to public with grant option;

table cupsort descr "Cupones para sorteos"
{
	emp	num(2)	descr "Codigo de Empresa"
					not null
					,
	nrosort	num(4)	descr "Numero de Sorteo"
					not null
					,
	nrocomp	num(8)	descr "Numero de Comprobante"
					not null
					,
	fimp	date	descr "Fecha de impresion del cupon"
					,
	himp	time	descr "Hora de impresion del cupon"
					,
}
primary key	(emp, nrosort, nrocomp) [ 15 ];
grant all on cupsort to public with grant option;

table jurxcomp descr "Jurisdiccion x Comprobantes"
{
	emp	num(2)	descr "Empresa"
					not null
					,
	tipcomp	num(4)	descr "Tipo del Comprobante"
					not null
					,
	sercomp	num(4)	descr "Serie del Comprobante"
					not null
					,
	nrocomp	num(9)	descr "Numero del Comprobante"
					not null
					,
	juris	num(2)	descr "En cual jurisdicción actuó la U.Op."
					not null
					,
	regim	num(3)	descr "Régimen de Retención"
					not null
					,
}
primary key	(emp, tipcomp, sercomp, nrocomp) [ 22 ];
grant all on jurxcomp to public with grant option;

table jurxemp descr "Juris de reten por Empresa"
{
	emp	num(2)	descr "Empresa"
					not null
					,
	juris	num(2)	descr "En cual jurisdicción actuó la U.Op."
					not null
					,
	regim	num(3)	descr "Régimen de Retención"
					not null
					,
}
primary key	(emp, juris, regim);
grant all on jurxemp to public with grant option;

table criterios descr "Crit. de clasificación de artículos"
{
	code	char(1)	descr "Código"
					not null
					,
	orden	num(2)	descr "Orden de procesamiento"
					not null
					
					check (this > 0),
	descrip	char(40)	descr "Descripción"
					not null
					,
}
primary key	(code),
unique index orden	(orden, code);
grant all on criterios to public with grant option;

table periodos descr "Períodos de análisis para listado estadístico"
{
	dias	num(3)	descr "Nro de días hasta la fecha actual"
					,
}
primary key	(dias);
grant all on periodos to public with grant option;

table tipific descr "Tipificación de parámetros"
{
	nomprog	char(12)	descr "Nombre del Programa"
					not null
					in progar:descrip,
	seq	num(2)	descr "Secuencia"
					not null
					
					check (this > 0),
	coment	char(80)	descr "Comentarios"
					not null
					,
	tgrpelem	char(1)	descr "Tipo de Grupo de los elementos"
					not null
					mask ">A"
					in ("G":"Grupos", "S":"Sucursales", "E":"Estados del STOCK", "M":"Estados del MOVIM", "O":"Oper. sobre arch.", "T":"Tipos de Mov."),
	tsubgr	char(1)	descr "Tipo de los elementos del subgrupo"
					mask ">A"
					in ("S":"Sucursales", "E":"Estados del STOCK", "M":"Estados del MOVIM", "O":"Oper. sobre arch.", "T":"Tipos de Mov."),
	grupdef	num(4)	descr "Grupo default"
					not null
					in grupos by tgrpelem(tgrpelem):descrip,
}
primary key	(nomprog, seq),
index grupdef	(grupdef);
grant all on tipific to public with grant option;

table logwar descr "Log de warnings"
{
	cdate	date	descr "Fecha del error"
					not null
					,
	ctime	time	descr "Hora del error"
					not null
					,
	pid	num(9)	descr "Process ID"
					not null
					,
	seq	num(3)	descr "Secuencia para esa fecha/hora/pid"
					not null
					default (0)
					
					check (this >= 0),
	modname	char(8)	descr "Nombre del programa"
					not null
					,
	terr	num(2)	descr "Tipo de error"
					not null
					in (1:"No existe en Catalogo", 2:"No hay stock suficiente"),
	codadm	char(8)	descr "Artículo"
					,
	depos	num(3)	descr "Depósito del movimiento"
					,
	estado	num(2)	descr "Estado del movimiento"
					,
}
primary key	(cdate, ctime, pid, seq) [ 4 ];
grant all on logwar to public with grant option;

table cfimpre descr "Impresoras Fiscales"
{
	codimpr	num(4)	descr "Código de Dispositivo"
					not null
					,
	descrip	char(30)	descr "Descripcion"
					not null
					,
	marca	num(1)	descr "Marca de la impresora"
					default (1)
					in (1:"Hasar P-323F", 2:"Hasar P-322F", 3:"Hasar P-321F", 4:"Hasar PL-8F", 5:"Hasar P425F", 6:"Hasar P-715F"),
	port	num(5)	descr "Instalada en port ?"
					not null
					default (10000)
					,
	timeout	num(3)	descr "Time Out en segundos"
					not null
					default (30)
					,
	estado	num(1)	descr "Disable/Enable"
					not null
					default (1)
					in (0:"Disable", 1:"Enable", 2:"Reparacion"),
	ptovta	num(4)	descr "Punto de Venta"
					not null
					,
	host	char(30)	descr "Host Asociado"
					,
	cuit	num(11)	descr "Nro de Cuit de la Empresa"
					,
	razsoc	char(50)	descr "Razon Social de la Empresa"
					,
	registro	char(10)	descr "Nro. de Registro"
					,
	finic	date	descr "Fecha de Inicializacion"
					,
	cfptovta	num(4)	descr "Nro. de Punto de Venta"
					,
	ingbr	char(30)	descr "Nro. de Ingresos Brutos"
					,
	finiact	date	descr "Fecha de Inicio de Actividades"
					,
	tiva	char(2)	descr "Responsabilidad frente al  iva"
					in ("I":"Responsable Inscripto", "N":"Responsable no Inscripto", "E":"Exento", "A":"No Responsable", "M":"Responsable Monotributo"),
	vouchers	char(1)	descr "Manejo de Vouchers"
					in ("H":"Por Doc. NFH", "T":"Por tickets Doc. NF"),
	desfa	num(4,2)	descr "Desfasaje de Hora"
					not null
					default (0.00)
					,
}
primary key	(codimpr),
index ptovta	(ptovta, estado, codimpr);
grant all on cfimpre to public with grant option;

table cfasig descr "Asignación de Impresoras"
{
	device	char(15)	descr "Devices"
					not null
					,
	codimpr	num(4)	descr "Código de Dispositivo"
					not null
					in cfimpre:descrip,
	estado	num(1)	descr "Activo/Inactivo"
					not null
					default (1)
					in (0:"Inactivo", 1:"Activo"),
	suc	num(3)	descr "Sucursal"
					not null
					,
	driver	num(1)	descr "Uso del Driver"
					not null
					default (1)
					in (0:"Offline", 1:"Linea a Linea Linux", 2:"X Comprobante Linux", 3:"Linea a Linea Hasar-Windows", 4:"X Comprobante Hasar-Windows", 9:"Linea a Linea port serial"),
	fechad	date	descr "Fecha Desde"
					default (null)
					,
	fechah	date	descr "Fecha Hasta"
					default (null)
					,
}
primary key	(device, codimpr),
unique index sucursal	(suc, estado, device, codimpr),
unique index codimpr	(codimpr, device),
unique index estado	(device, estado, codimpr);
grant all on cfasig to public with grant option;

table cfserie descr "Series de Impresoras"
{
	codimpr	num(4)	descr "Código de Dispositivo"
					not null
					,
	serie	num(4)	descr "Serie"
					not null
					,
}
primary key	(codimpr);
grant all on cfserie to public with grant option;

table cftipdoc descr "Cross Tipos de Documentos"
{
	tdoccf	char(1)	descr "Tipo de Documento de C.Fiscal"
					not null
					in ("C":"CUIT", "0":"Libreta de enrolamiento", "1":"Libreta civica", "2":"DNI", "3":"Pasaporte", "4":"Cedula de identidad"),
	tdoc	char(3)	descr "Tipo de Documento Propio"
					not null
					,
}
primary key	(tdoccf, tdoc),
unique index tipdoc	(tdoc);
grant all on cftipdoc to public with grant option;

table cftipcom descr "Cross Tipos de Comprobante"
{
	codigo	num(3)	descr "Codigo de Tipo de Comprobante"
					not null
					,
	descrip	char(30)	descr "Descr. del Tipo de Comprobante"
					not null
					,
	reduc	char(5)	descr "Descr. reducida"
					not null
					,
	tcompvta	num(3)	descr "Tipo de Movimiento de Ventas"
					,
	tcompaur	num(3)	descr "Tipo de Movimiento de Aurus"
					,
	letravta	char(2)	descr "Letra a Imprimir"
					,
	letracf	char(2)	descr "Tipo de Comprobante que se envia al Controlador"
					not null
					in ("A":"Factura A", "B":"Factura B o C", "a":"Recibo A", "b":"Recibo B o C", "D":"Nota de Debito A", "E":"Nota de Debito B o C", "R":"Nota de Credito A", "S":"Nota de Credito B o C", "r":"Remito", "s":"Orden de Salida", "t":"Resumen de Cuenta", "u":"Coitzacion", "x":"Recibo X", ":":"Clausulas de Credito en Cuotas Fijas", "<":"Pagare", "=":"Poliza de Seguro de Garantia Complementaria", "?":"Solicitud de Credito"),
	tcf	char(1)	descr "Tipo de Cbte Controlador Fiscal"
					not null
					default ("F")
					in ("H":"No fiscal Homologado", "F":"Fiscal", "N":"No fiscal"),
}
primary key	(codigo);
grant all on cftipcom to public with grant option;

table cftiva descr "Cross Tipos de Iva"
{
	tivacf	char(1)	descr "Tipo de Iva de C.Fiscal"
					not null
					in ("I":"Responsable inscripto", "N":"Responsable no inscripto", "E":"Exento", "A":"No responsable", "C":"Consumidor final", "B":"Resp.no inscripto vta.bienes uso", "M":"Resp. monotributo", "T":"No categorizado"),
	tiva	num(2)	descr "Tipo de Iva Propio"
					not null
					,
}
primary key	(tivacf, tiva),
unique index tiva	(tiva);
grant all on cftiva to public with grant option;

table cfserfac descr "Nros de Series de Facturación"
{
	tipofac	char(1)	descr "Tipo de Comprobante de C.Fiscal"
					not null
					in ("X":"Recibo X", "S":"Senia X"),
	serie	num(4)	descr "Nro de Serie"
					not null
					,
	fecha	date	descr "Fecha de Vigencia"
					not null
					,
	nroact	num(8)	descr "Nro actual de la Serie"
					not null
					,
	minimo	num(8)	descr "Mínimo de la Serie"
					not null
					,
	maximo	num(8)	descr "Maximo de la Serie"
					not null
					,
	nroalarm	num(8)	descr "Alarma"
					,
	activo	bool	descr "Activo"
					not null
					default (0)
					,
	fecalta	date	descr "Fecha de alta en DGI"
					not null
					,
	fecbaja	date	descr "Fecha de baja en DGI"
					,
	cdate	date	descr "Fecha de Carga"
					,
	ctime	time	descr "Hora de Carga"
					,
	cuid	num(5)	descr "Usuario que Cargo"
					,
	mdate	date	descr "Fecha de Modificacion"
					,
	mtime	time	descr "Hora de Modificacion"
					,
	muid	num(5)	descr "Usuario que Modifico"
					,
}
primary key	(tipofac, serie),
index serie	(serie);
grant all on cfserfac to public with grant option;

table loggral descr "Logs de Modificaciones Generales"
{
	tipo	num(4)	descr "Tipo de LOG"
					not null
					,
	codigo	char(15)	descr "Código de Item del LOG"
					not null
					,
	fecha	date	descr "Fecha Modificacion"
					not null
					,
	hora	time	descr "Hora Modificacion"
					not null
					,
	usuario	num(5)	descr "Fecha Modificacion"
					not null
					,
	descrip	char(50)	descr "Descripcion de la Modificación"
					not null
					,
}
primary key	(tipo, codigo, fecha, hora) [ 15 ];
grant all on loggral to public with grant option;

table ctayord descr "Datos de Cuenta y Orden"
{
	codigo	num(2)	descr "Código"
					not null
					,
	descor	char(8)	descr "Descripcion corta"
					not null
					,
	descrip	char(50)	descr "Descripcion"
					not null
					,
	vendesolo	bool	descr "Si no se imprime con otros artic simult"
					not null
					default (1)
					,
	conleyenda	bool	descr "Sale la leyenda de Cta y Ord en L.Iva?"
					not null
					default (1)
					,
	obs1	char(80)	descr "Renglon 1 de leyenda"
					,
	obs2	char(80)	descr "Renglon 2 de leyenda"
					,
	tipoval	num(3)	descr "Cod del importe bruto de esa CyO en TIPVAL"
					,
	grartic	num(2)	descr "Grupo de articulos (Codifica 56)"
					,
	tipocyo	char(1)	descr "Factura o Cobra por C y O"
					not null
					default ("F")
					mask ">A"
					in ("F":"Factura por C y O", "C":"Cobra por C y O"),
	cctei	num(6)	descr "Cta Cte de imputacion financiera"
					not null
					,
	trat_esp	num(1)	descr "Si tiene tratamiento especial de impuestos"
					not null
					default (2)
					in (0:"Garantia Extendida", 1:"NO gravado", 2:"Gravado"),
	apynom	bool	descr "Envia Ap. y Nom. en archivo ASCII ?"
					not null
					default (0)
					,
	tyndoc	bool	descr "Envia Tipo y Nro Doc. en archivo ASCII ?"
					not null
					default (0)
					,
	tiva	num(2)	descr "Tipo de Iva : A/B/AyB"
					,
	dtoesp	bool	descr "Permite Descuento Especial?"
					not null
					default (0)
					,
	dtofin	bool	descr "Permite Descuento Financiero?"
					not null
					default (0)
					,
	fecgrav	date	descr "Fecha desde la cual es gravado"
					,
	ivaexe	bool	descr "Permite Iva Exento"
					not null
					default (0)
					,
	ctaogral	num(2)	descr "Codigo cta.ord. general"
					,
	difcta	bool	descr "Diferencia Cuenta"
					not null
					default (0)
					,
	activa	bool	descr "Activa"
					not null
					default (1)
					,
	rindeart	bool	descr "Rinde Artículos?"
					not null
					default (0)
					,
	cuit	char(15)	descr "CUIT/CUIL"
					,
}
primary key	(codigo),
index grartic	(grartic);
grant all on ctayord to public with grant option;

table vtasucap descr "Declaracion de Ventas por sucursal "
{
	tipomov	num(2)	descr "Tipo de movimiento"
					not null
					,
	numcomp	num(8)	descr "Nro. de comprobante"
					not null
					,
	lote	num(8)	descr "Nro de lote"
					not null
					,
	suc	num(3)	descr "Sucursal"
					not null
					,
	fecha	date	descr "Fecha del Comprobante"
					,
	hora	time	descr "Hora del Comprobante"
					,
	letra	char(1)	descr "Letra del Comprobante"
					,
	puntovta	char(4)	descr "Punto de Venta"
					,
	nrofac	char(8)	descr "Numero de Factura"
					,
	tipcomp	char(1)	descr "Tipo de Comprobante"
					,
	importe	num(11,2)	descr "Importe sin Iva"
					default (0.00)
					,
	iva	num(11,2)	descr "Importe de Iva"
					default (0.00)
					,
	otrosimp	num(11,2)	descr "Importe de otros impuesto"
					default (0.00)
					,
	cdate	date	descr "Fecha de informe"
					,
	ctime	time	descr "Hora de informe"
					,
	cuid	num(5)	descr "Responzable del informe"
					,
}
primary key	(tipomov, numcomp) [ 12 ],
index lote	(lote, suc) [ 12 ];
grant all on vtasucap to public with grant option;

table tarflet descr "Tarifas de los fletes"
{
	deposito	num(3)	descr "Depósito origen de Hoja de Ruta"
					not null
					,
	codigo	num(3)	descr "Código de Tarifa"
					not null
					,
	descrip	char(40)	descr "Descripcion"
					not null
					,
}
primary key	(deposito, codigo);
grant all on tarflet to public with grant option;

table tarxdest descr "Tarifas por destino"
{
	deposito	num(3)	descr "Depósito origen de Hoja de Ruta"
					not null
					,
	codigo	num(3)	descr "Código de Tarifa"
					not null
					,
	codflia	char(8)	descr "Familia"
					mask $codflia
					,
	area	num(3)	descr "Area"
					in areas(deposito):descrip,
	mentrega	char(1)	descr "Modo de entrega"
					not null
					mask ">A"
					in ("D":"Domiciliarias", "E":"Express", "T":"Venta telefonica interior"),
	pribulto	num(6,2)	descr "Importe del primer bulto"
					,
	incbulto	num(6,2)	descr "Importe incremento por bulto"
					,
}
primary key	(deposito, codigo, mentrega, area, codflia);
grant all on tarxdest to public with grant option;

table tarxtrans descr "Relacion Tarifa-Trasportista"
{
	deposito	num(3)	descr "Depósito origen de Hoja de Ruta"
					not null
					,
	trans	num(9)	descr "Transportista en AURUS"
					not null
					,
	codigo	num(3)	descr "Código de Tarifa"
					not null
					,
	tipo	char(1)	descr "Tarifa especial o normal"
					not null
					default ("N")
					mask ">A"
					in ("E":"Especial", "N":"Normal"),
}
primary key	(deposito, codigo, trans),
unique index transp	(deposito, trans, tipo);
grant all on tarxtrans to public with grant option;

table prefactu descr "Cabecera de pre-factura"
{
	prefa	num(8)	descr "Hoja de Ruta"
					not null
					,
	nroconf	num(8)	descr "Numero de Confirme"
					not null
					,
	estado	char(1)	descr "Estado de la pre-factura"
					not null
					default ("A")
					mask ">A"
					in ("A":"Abierta", "E":"Con errores", "C":"Cerrada", "I":"Contabilizada", "F":"Facturada", "D":"Recib.en Deposito", "B":"Recib.en Baigorria", "R":"Rechaz.p/ Baigorria", "T":"En trans.Ctas.a Pag.", "U":"Recib.en Ctas.a Pag.", "Z":"Rechaz.por Ctas.a Pag.", "N":"En transito Tesoreria", "S":"Recib.en Tesoreria"),
	tipcomp	num(4)	descr "T.C. de Aurus"
					,
	serie	num(4)	descr "S.C. de Aurus"
					,
	nrocomp	num(9)	descr "N.C. de Aurus"
					,
	deposito	num(3)	descr "Depósito origen de Hoja de Ruta"
					not null
					,
	trans	num(9)	descr "Transportista en AURUS"
					not null
					,
	fecha	date	descr "Fecha de la hoja de ruta"
					not null
					,
	transorig	num(9)	descr "Transportista Original"
					,
	emptrans	num(3)	descr "Emp. legajo que modifico el Transportista"
					,
	nrolegtrans	num(9)	descr "Legajo que modifico el transportista"
					,
	usucerro	num(5)	descr "Usuario que cerro la prefactura"
					,
	fcierre	date	descr "Fecha de Cierre de la prefactura"
					,
	hcierre	time	descr "Hora Cierre de la prefactura"
					,
	depalt	num(3)	descr "Deposito que utiliza para valorizar"
					not null
					,
	cuid	num(5)	descr "Usuario que cargo prefactura"
					,
	cdate	date	descr "Fecha de Carga"
					,
	ctime	time	descr "Hora de Carga"
					,
}
primary key	(prefa, nroconf) [ 7 ],
unique index depos	(deposito, trans, estado, fecha, prefa, nroconf) [ 12 ],
index estado	(estado, fecha) [ 4 ],
index fecha	(fecha) [ 4 ];
grant all on prefactu to public with grant option;

table renprefa descr "Renglon de la pre-factura"
{
	prefa	num(8)	descr "Hoja de Ruta"
					not null
					,
	nroconf	num(8)	descr "Numero de Confirme"
					not null
					,
	renglon	num(3)	descr "Renglon"
					not null
					
					check (this > 0),
	prov	char(1)	descr "Provincia Destino"
					not null
					mask "1>x"
					,
	local	num(5)	descr "Localidad Destino"
					not null
					,
	area	num(3)	descr "Codigo de area"
					,
	direcc	char(45)	descr "Direccion Destino"
					not null
					,
	importe	num(7,2)	descr "Valorizacion"
					not null
					,
	cmotman	num(2)	descr "Motivo Manual"
					,
}
primary key	(prefa, nroconf, renglon) [ 14 ];
grant all on renprefa to public with grant option;

table motcar descr "Motivos de cargos a fletes"
{
	motivo	num(2)	descr "Motivo del cargo"
					not null
					,
	descrip	char(40)	descr "Descripcion"
					not null
					,
	signo	num(1)	descr "Signo del cargo"
					not null
					in (1:"Positivo", -1:"Negativo"),
	maximo	num(7,2)	descr "Importe maximo"
					,
	minimo	num(7,2)	descr "Importe minimo"
					,
}
primary key	(motivo);
grant all on motcar to public with grant option;

table carflet descr "Cargos por flete"
{
	prefa	num(8)	descr "Nro de pre-factura"
					not null
					,
	nroconf	num(8)	descr "Numero de Confirme"
					not null
					in prefactu(prefa),
	renprefa	num(3)	descr "Renglon pre-factura"
					not null
					in renprefa(prefa, nroconf)
					check (this > 0),
	renglon	num(3)	descr "Renglon"
					not null
					,
	motivo	num(2)	descr "Motivo del cargo"
					not null
					in motcar:descrip,
	importe	num(7,2)	descr "Valorizacion"
					not null
					,
}
primary key	(prefa, nroconf, renprefa, renglon) [ 12 ];
grant all on carflet to public with grant option;

table vtasucod descr "Tabla de codificadores p/Vtas. p/Suc."
{
	tipo	num(1)	descr "Tipo de codificador"
					not null
					,
	id	num(2)	descr "Identificacion del codificador"
					not null
					,
	descrip	char(20)	descr "Descripcion"
					not null
					,
	valor	char(2)	descr "Valor asociado"
					,
	cdate	date	descr "Fecha de Carga"
					,
	ctime	time	descr "Hora de Carga"
					,
	cuid	num(5)	descr "Usuario que Cargo"
					,
}
primary key	(tipo, id);
grant all on vtasucod to public with grant option;

table valecaj descr "Vales de Caja"
{
	tipomov	num(2)	descr "Tipo de Movimiento"
					not null
					,
	numcomp	num(8)	descr "Nro. de Comprobante"
					not null
					,
	sucursal	num(3)	descr "Sucursal"
					not null
					,
	fecha	date	descr "Fecha"
					not null
					default (today)
					,
	cajero	num(5)	descr "Cajero"
					in cajeros(sucursal):descrip,
	convale	num(2)	descr "Concepto del Vale"
					not null
					,
	impvale	num(2)	descr "Imputacion del Vale"
					not null
					,
	concont	num(2)	descr "Concepto Contrapartida"
					not null
					,
	impcont	num(2)	descr "Imputacion Contrapartida"
					not null
					,
	monto	num(11,2)	descr "Monto"
					not null
					,
	refer	num(11)	descr "Referencia Contrapartida"
					,
	tipcot	num(2)	descr "Moneda de Cotizacion"
					,
	nromovc	num(8)	descr "Nro. de Mov. de Caja relacionado"
					,
	motivo	num(2)	descr "Motivo o Concepto del Vale"
					,
	estado	char(1)	descr "Estado"
					not null
					default ("I")
					in ("I":"Inicio", "G":"Aprobado Gerente", "R":"Rechazado por Administracion", "P":"Pendiente de Impresion", "A":"Aprobado por Administracion"),
	nroleg	num(9)	descr "Legajo que autoriza en Central"
					,
	remite	num(9)	descr "Legajo a quien el cajero pide autorizacion"
					,
	legref	num(9)	descr "Legajo del Referente o Aprob. Gerencial"
					,
	adjfact	bool	descr "Adjunta Factura"
					not null
					default (0)
					,
	adjreci	bool	descr "Adjunta Recibo"
					not null
					default (0)
					,
	adjmail	bool	descr "Adjunta Mail"
					not null
					default (0)
					,
	adjcpag	bool	descr "Adjunta Cbte. de Pago"
					not null
					default (0)
					,
	enroleg	num(3)	descr "Empresa legajo que autoriza en central"
					,
	eremite	num(3)	descr "Empresa legajo a quien el cajero pide autorizacion"
					,
	elegref	num(3)	descr "Empresa legajo del referente o aprob. gerencial"
					,
	cuid	num(5)	descr "Usuario que Cargo"
					,
	cdate	date	descr "Fecha de Carga"
					,
	ctime	time	descr "Hora de Carga"
					,
	muid	num(5)	descr "Usuario que Modifico"
					,
	mdate	date	descr "Fecha de Modificacion"
					,
	mtime	time	descr "Hora de Modificacion"
					,
}
primary key	(tipomov, numcomp) [ 4 ],
unique index sucur	(sucursal, tipomov, numcomp) [ 6 ],
unique index estado	(estado, tipomov, numcomp) [ 6 ],
index cajero	(sucursal, fecha, cajero) [ 6 ],
index respon	(eremite not null, remite not null, estado) [ 4 ];
grant all on valecaj to public with grant option;

table areas descr "Areas para tarifar fletes"
{
	deposito	num(3)	descr "Depósito origen"
					not null
					,
	codigo	num(3)	descr "Codigo de area"
					not null
					,
	descrip	char(10)	descr "Descripcion"
					not null
					,
	grupoh	num(2)	descr "Grupo Horario"
					,
}
primary key	(deposito, codigo),
unique index descrip	(deposito, descrip);
grant all on areas to public with grant option;

table areasxloc descr "Areas por Provincia y Localidad"
{
	deposito	num(3)	descr "Depósito origen"
					not null
					,
	codigo	num(3)	descr "Codigo de area"
					not null
					,
	prov	char(1)	descr "Provincia"
					not null
					mask "1>x"
					,
	local	num(5)	descr "Código de Localidad"
					,
}
primary key	(deposito, codigo, prov, local),
unique index prov	(deposito, prov, local),
index local	(prov, local);
grant all on areasxloc to public with grant option;

table remihr descr "Remitos por Hoja de Ruta"
{
	prefa	num(8)	descr "Nro de pre-factura"
					not null
					,
	nroconf	num(8)	descr "Numero de Confirme"
					not null
					in prefactu(prefa),
	renprefa	num(3)	descr "Renglon pre-factura"
					not null
					in renprefa(prefa, nroconf)
					check (this > 0),
	tipomov	num(2)	descr "NVI / NCI"
					not null
					,
	numcomp	num(8)	descr "Número de NVI / NCI"
					not null
					,
	remito	num(8)	descr "Remito Interno"
					not null
					,
}
primary key	(prefa, nroconf, renprefa, tipomov, numcomp, remito) [ 17 ];
grant all on remihr to public with grant option;

table prexcant descr "Precio del flete por cantidad de bultos"
{
	deposito	num(3)	descr "Depósito origen de Hoja de Ruta"
					not null
					,
	codigo	num(3)	descr "Código de Tarifa"
					not null
					,
	codflia	char(8)	descr "Familia"
					mask $codflia
					,
	area	num(3)	descr "Area"
					in areas(deposito):descrip,
	mentrega	char(1)	descr "Modo de entrega"
					not null
					mask ">A"
					in ("D":"Domiciliarias", "E":"Express", "T":"Venta telefonica interior"),
	bultos	num(2)	descr "Cant. de bultos"
					,
	bulfac	num(2)	descr "Cant. de bultos a facturar"
					,
}
primary key	(deposito, codigo, mentrega, area, codflia, bultos);
grant all on prexcant to public with grant option;

table motrman descr "Motivos para realizar comprobantes Manuales"
{
	motivo	num(3)	descr "Motivo"
					not null
					,
	descrip	char(40)	descr "Descripción Larga"
					not null
					,
	descor	char(15)	descr "Descripción Corta"
					not null
					,
	tipcab	num(2)	descr "Tipo de Comprobante Cabecera"
					not null
					,
	tipnocab	num(2)	descr "Tipo de Comprobante No Cabecera"
					,
	tipcomasoc	num(2)	descr "Tipo de Comprobante Asociado"
					,
	sucsolic	bool	descr "Se pide la sucursal solicitante?"
					not null
					,
	genauto	bool	descr "Generación automática del comprobante no cabecera?"
					not null
					,
	asocri	bool	descr "Asocia remito Carta de Porte?"
					not null
					,
	ctrlcant	bool	descr "Controla cantidad"
					not null
					,
}
primary key	(motivo);
grant all on motrman to public with grant option;

table rmotrman descr "Renglones de los Motivos"
{
	motivo	num(3)	descr "Motivo"
					not null
					in motrman:descrip,
	tipomov	num(2)	descr "Tipo de Movimiento Manual Cab/No Cab"
					not null
					,
	tiprel	num(2)	descr "Tipo de Movimiento Relacionado (Proceso)"
					not null
					,
	scodorig	num(3)	descr "Estado origen"
					,
	scoddest	num(3)	descr "Estado destino"
					,
}
primary key	(motivo, tipomov, tiprel);
grant all on rmotrman to public with grant option;

table pmotrman descr "Permisos por Motivos"
{
	motivo	num(3)	descr "Motivos"
					not null
					in motrman:descrip,
	empleg	num(2)	descr "Empresa del legajo"
					not null
					,
	legajo	num(7)	descr "Legajo"
					not null
					,
}
primary key	(motivo, empleg, legajo),
unique index legajo	(empleg, legajo, motivo);
grant all on pmotrman to public with grant option;

table remman descr "Remitos y Ordenes de Retiro Manual"
{
	tipomov	num(2)	descr "Tipo de movimiento Manual (RM/ORM)"
					not null
					,
	numcomp	num(8)	descr "Número de comprobante Manual"
					not null
					,
	fecha	date	descr "Fecha de registración del comprobante"
					not null
					,
	sucursal	num(3)	descr "Sucursal que genera el movimiento"
					not null
					,
	motivo	num(3)	descr "Motivo"
					not null
					in motrman:descrip,
	sucsolic	num(3)	descr "Sucursal solicitante"
					,
	tiprel	num(2)	descr "Tipo de comprobante Relacionado"
					,
	numrel	num(8)	descr "Número de comprobante Relacionado"
					,
	tiprorm	num(2)	descr "Tipo de Rem/OR Manual Relacionado"
					,
	numrorm	num(8)	descr "Número de comprobante Manual Relacionado"
					,
	empleg	num(2)	descr "Empresa del Legajo"
					,
	legajo	num(7)	descr "Nro de Legajo"
					,
	cliente	num(8)	descr "Cód. del cliente"
					,
	clase	num(2)	descr "Clase de Dirección : Dir"
					,
	coddir	num(4)	descr "Código de Dirección"
					,
	nrolista	num(6)	descr "Número de Lista de Casamiento"
					,
	libre	bool	descr "Existe el comprobante relacionado?"
					,
	fecent	date	descr "Fecha de entrega"
					not null
					,
	horent	num(2)	descr "Horario de entrega"
					not null
					,
	remito	num(8)	descr "Remito de Carta de Porte del Compo. Asociado"
					,
	mdate	date	descr "Fecha de Modificacion"
					,
	mtime	time	descr "Hora de Modificacion"
					,
	muid	num(5)	descr "Usuario que Modifico"
					,
}
primary key	(tipomov, numcomp);
grant all on remman to public with grant option;

table detprefa descr "Detalle Renglon de la pre-factura"
{
	prefa	num(8)	descr "Hoja de Ruta"
					not null
					,
	nroconf	num(8)	descr "Numero de Confirme"
					not null
					,
	renglon	num(3)	descr "Renglon"
					not null
					
					check (this > 0),
	tipomov	num(2)	descr "NVI / NCI"
					,
	numcomp	num(8)	descr "Número de NVI / NCI"
					,
	sucursal	num(3)	descr "Sucursal de venta"
					not null
					,
	porc	num(5,2)	descr "Porcentaje del importe"
					,
	importe	num(7,2)	descr "Valorizacion"
					not null
					,
	impadic	num(7,2)	descr "Importe cargos adicionales"
					not null
					default (0.00)
					,
	imptot	num(7,2)	descr "Importe Total"
					not null
					,
}
primary key	(prefa, nroconf, renglon, tipomov, numcomp) [ 19 ],
index comp	(tipomov, numcomp) [ 9 ];
grant all on detprefa to public with grant option;

table motmanvf descr "Motivos Manuales de fletes"
{
	cmotman	num(2)	descr "Motivo Manual"
					not null
					,
	descrip	char(40)	descr "Descripcion"
					not null
					,
	asocia	bool	descr "Se asocia a nvi/nci?"
					not null
					,
	maximo	num(7,2)	descr "Importe maximo"
					,
	minimo	num(7,2)	descr "Importe minimo"
					,
	ctacble	num(6)	descr "Cuenta contable"
					,
	ctaalt	num(6)	descr "Cuenta contable Alternativa"
					,
}
primary key	(cmotman);
grant all on motmanvf to public with grant option;

table tarjaniv descr "Tarjeta Aniversario"
{
	codcta	num(8)	descr "Código de Cuenta"
					not null
					
					check (this > 0),
	tipomov	num(2)	descr "Tipo de Movimiento Relacionado"
					,
	numcomp	num(8)	descr "Número de Modif."
					,
	renglon	num(4)	descr "Renglón"
					not null
					,
	fecha	date	descr "Fecha"
					not null
					default (today)
					,
	hora	time	descr "Hora"
					not null
					default (hour)
					,
	sucursal	num(3)	descr "Sucursal"
					not null
					,
	impresa	num(1)	descr "Impresa ?"
					default (0)
					,
	poseetc	bool	descr "Posee Tarjeta de Credito"
					not null
					default (1)
					,
}
primary key	(codcta, renglon) [ 14 ],
unique index sucursal	(sucursal, impresa, fecha, hora, codcta) [ 19 ];
grant all on tarjaniv to public with grant option;

table agrsubm descr "Grupo de Submedios de Pago"
{
	tipo	num(2)	descr "Tipo de Grupo"
					not null
					,
	subt	num(2)	descr "SubTipo de Grupo"
					not null
					,
	mpago	num(2)	descr "Medio de Pago"
					not null
					,
	subm	num(3)	descr "Submedio de Pago"
					not null
					,
}
primary key	(tipo, subt, mpago, subm);
grant all on agrsubm to public with grant option;

table valmps descr "Valores por Medio de Pago y Sucursal"
{
	tipo	num(2)	descr "Tipo de Valor"
					not null
					,
	suc	num(3)	descr "Sucursal"
					not null
					,
	codigo	num(2)	descr "Código del Medio de Pago"
					not null
					,
	subcod	num(3)	descr "Desglose de los M. Pagos"
					not null
					,
	cuotas	num(2)	descr "Cantidad de cuotas - pagos"
					,
	fechad	date	descr "Fecha de vigencia (desde)"
					not null
					,
	fechah	date	descr "Fecha de vigencia (Hasta)"
					,
	valor	num(4,2)	descr "Valor"
					not null
					,
}
primary key	(tipo, suc, codigo, subcod, cuotas, fechad) [ 3 ],
index fecha	(tipo, suc, fechah, codigo) [ 3 ];
grant all on valmps to public with grant option;

table fliasval descr "Familias sin Valorizacion de Fletes"
{
	codflia	char(8)	descr "Código de familia"
					not null
					,
}
primary key	(codflia);
grant all on fliasval to public with grant option;

table cfcai descr "Nros de CAI"
{
	tipofac	char(1)	descr "Tipo de Comprobante de C.Fiscal"
					not null
					in ("A":"Factura A", "B":"Factura B o C", "a":"Recibo A", "b":"Recibo B o C", "D":"Nota de Debito A", "E":"Nota de Debito B o C", "R":"Nota de Credito A", "S":"Nota de Credito B o C", "r":"Remito", "t":"Resumen de Cuenta", "x":"Recibo X"),
	serie	num(4)	descr "Nro de Serie"
					not null
					,
	fdesde	date	descr "Fecha desde"
					not null
					,
	nrocai	char(20)	descr "Nro de CAI"
					not null
					,
	fhasta	date	descr "Fecha Hasta"
					not null
					,
}
primary key	(tipofac, serie, fdesde);
grant all on cfcai to public with grant option;

table cargxcomp descr "Cargos por Comprobante"
{
	prefa	num(8)	descr "Hoja de Ruta"
					not null
					,
	nroconf	num(8)	descr "Numero de Confirme"
					not null
					,
	renglon	num(3)	descr "Renglon"
					not null
					
					check (this > 0),
	tipomov	num(2)	descr "NVI / NCI"
					,
	numcomp	num(8)	descr "Número de NVI / NCI"
					,
	motivo	num(2)	descr "Motivo del cargo"
					not null
					in motcar:descrip,
	importe	num(7,2)	descr "Valorizacion"
					not null
					,
}
primary key	(prefa, nroconf, renglon, tipomov, numcomp, motivo);
grant all on cargxcomp to public with grant option;

table vfagrup descr "Cabecera Agrupamiento de pre-facturas"
{
	tipcomp	num(4)	descr "T.C. de Aurus"
					not null
					,
	serie	num(4)	descr "S.C. de Aurus"
					not null
					,
	nrocomp	num(9)	descr "N.C. de Aurus-Nro.Agrupamiento"
					not null
					,
	estado	char(1)	descr "Estado de la pre-factura"
					not null
					default ("A")
					mask ">A"
					in ("A":"Abierta", "C":"Cerrada", "I":"Contabilizada", "F":"Facturada", "D":"Recib.en Deposito", "B":"Recib.en Baigorria", "R":"Rechaz.p/ Baigorria", "T":"En trans.Ctas.a Pag.", "U":"Recib.en Ctas.a Pag", "Z":"Rechaz.por Ctas.a Pag.", "N":"En transito Tesoreria", "S":"Recib.en Tesoreria"),
	nrofact	char(13)	descr "Nro. Factura Transportista"
					,
	deposito	num(3)	descr "Depósito origen de Hoja de Ruta"
					not null
					,
	trans	num(9)	descr "Transportista en AURUS"
					not null
					,
	nroccte	num(6)	descr "Número de Cuenta Corriente"
					,
	fecha	date	descr "Fecha del comprobante de agrupamiento"
					not null
					,
	usuario	num(5)	descr "Usuario que genero"
					,
	mentrega	char(1)	descr "Modo de entrega"
					mask ">A"
					in ("D":"Domiciliarias", "E":"Express", "T":"Venta telefonica interior", "N":"No valorizada"),
}
primary key	(tipcomp, serie, nrocomp) [ 4 ],
index estado	(estado, fecha) [ 2 ];
grant all on vfagrup to public with grant option;

table agrprefa descr "Renglones Agrupamiento de pre-facturas"
{
	tipcomp	num(4)	descr "T.C. de Aurus"
					not null
					,
	serie	num(4)	descr "S.C. de Aurus"
					not null
					,
	nrocomp	num(9)	descr "N.C. de Aurus-Nro.Agrupamiento"
					not null
					,
	prefa	num(8)	descr "Hoja de Ruta"
					not null
					,
	nroconf	num(8)	descr "Numero de Confirme"
					not null
					,
}
primary key	(tipcomp, serie, nrocomp, prefa, nroconf) [ 12 ],
index prefa	(prefa, nroconf) [ 7 ];
grant all on agrprefa to public with grant option;

table logcbte descr "Log de autorizaciones"
{
	tipomov	num(2)	descr "Tipo de Movimiento"
					not null
					,
	numcomp	num(8)	descr "Número de movimiento"
					,
	renglon	num(4)	descr "Renglón"
					not null
					
					check (this >= 0),
	tipolog	num(2)	descr "Tipo de log"
					not null
					,
	empleg	num(2)	descr "Empresa del legajo"
					not null
					,
	legajo	num(7)	descr "Legajo"
					not null
					,
	estado	char(1)	descr "Estado en que queda el Comprobante"
					,
	cdate	date	descr "Fecha de Carga"
					,
	ctime	time	descr "Hora de Carga"
					,
	cuid	num(5)	descr "Usuario que Cargo"
					,
	mdate	date	descr "Fecha de Modificacion"
					,
	mtime	time	descr "Hora de Modificacion"
					,
	muid	num(5)	descr "Usuario que Modifico"
					,
}
primary key	(tipomov, numcomp, renglon) [ 11 ],
index tipo	(tipolog, cdate) [ 8 ];
grant all on logcbte to public with grant option;

table moneda descr "Monedas"
{
	codmon	num(2)	descr "Código de Moneda"
					not null
					,
	descrip	char(30)	descr "Descripcion larga"
					not null
					,
	descor	char(10)	descr "Descripcion corta"
					not null
					,
	simbolo	char(5)	descr "Simbolo"
					not null
					,
	imprime	bool	descr "Imprime leyenda ?"
					not null
					,
	leyenda1	char(90)	descr "Leyenda 1"
					,
	leyenda2	char(90)	descr "Leyenda 2"
					,
	clegal	bool	descr "Es Moneda de Curso Legal"
					,
	monade	num(2)	descr "Moneda para generar el adelanto"
					not null
					default (0)
					in moneda:descrip,
	currcode	char(15)	descr "Moneda de EBS"
					,
}
primary key	(codmon),
unique index legal	(clegal not null);
grant all on moneda to public with grant option;

table cfreplan descr "Configuraciones automaticas para el replan"
{
	nro	num(3)	descr "Numero"
					not null
					
					check (this > 0),
	descrip	char(30)	descr "Descripcion"
					not null
					,
	dias	num(2)	descr "Dias"
					,
	selartic	bool	descr "Selecciona por articulos"
					,
	criterio	[10] char(1)	descr "Criterios"
					,
	categoria	[10] num(1)	descr "Categorias"
					,
	ensucu	char(5)	descr "Distribucion : Stock en sucursal"
					,
	endepos	char(5)	descr "Distribucion : Stock en deposito"
					,
	diasstock	num(1)	descr "Calculo de dias de stock"
					,
	venta0	char(5)	descr "Venta cero"
					,
	deposppal	num(3)	descr "Deposito central"
					,
	grsucdep	num(3)	descr "Otros depositos centrales"
					,
	nroest	char(7)	descr "Grupo de estados pedido de sucursal"
					,
	valor	num(1)	descr "Valorizado a"
					,
	pedir	num(2)	descr "Pedir/devolver"
					,
	tipo	num(1)	descr "Tipo"
					,
	tipo2	char(1)	descr "Cantidad/Pesos"
					,
	orden	num(1)	descr "Ordenado por"
					,
	grupo	num(2)	descr "Grupo de articulos"
					,
	modalidad	num(1)	descr "Modalidades de uso"
					in (1:"Para foto Polaroid", 2:"Pedido bloqueador"),
	sgrsuc	num(4)	descr "Super grupo de sucursales"
					,
	selec	char(2)	descr "Seleccion"
					,
	lineas	[10] num(2)	descr "Lineas de articulos"
					,
	agrup	num(2)	descr "Agrupamiento de configuraciones"
					,
}
primary key	(nro),
index modal	(modalidad, nro);
grant all on cfreplan to public with grant option;

table pedautom descr "Pedido bloqueador del replan"
{
	grsuc	num(4)	descr "Grupo de Sucursales"
					not null
					,
	codadm	char(8)	descr "Código de administración"
					not null
					mask $codadm
					,
	fecha	date	descr "Fecha"
					not null
					default (today)
					,
	cant	num(6)	descr "Cantidad sugerida"
					not null
					default (0)
					,
}
primary key	(grsuc, codadm, fecha);
grant all on pedautom to public with grant option;

table fotorep descr "Fotos del replan"
{
	grsuc	num(4)	descr "Grupo de Sucursales"
					not null
					,
	fecha	date	descr "Fecha"
					not null
					default (today)
					,
	modelo	num(3)	descr "Modelo"
					not null
					in cfreplan:descrip,
	valor	num(12,2)	descr "Importe/valor calculado"
					not null
					default (0.00)
					,
}
primary key	(grsuc, fecha, modelo) [ 11 ];
grant all on fotorep to public with grant option;

table partidas descr "Informacion de partidas de C.Informe de Recep."
{
	codadm	char(8)	descr "Código de administración"
					not null
					mask $codadm
					,
	tipconf	num(2)	descr "Tipo de movimiento C.I.R."
					not null
					,
	nroconf	num(8)	descr "Numero de comprobante C.I.R."
					not null
					,
	fecconf	date	descr "Fecha C.I.R."
					not null
					default (today)
					,
	cantconf	num(6)	descr "Cantidad C.I.R."
					not null
					default (0)
					,
	saldo	num(6)	descr "Saldo x vender de esta partida"
					not null
					default (0)
					,
	fp_oc	date	descr "Fecha de pago Orden de Compra"
					,
	fp_fac	date	descr "Fecha de pago Factura"
					,
	fp_medpag	date	descr "Fecha de pago Medio de Pago"
					,
	activa	bool	descr "Partida activa"
					not null
					default (1)
					,
	costo	num(12,2)	descr "Costo de reposicion"
					,
	resultado	num(10,2)	descr "Perdida o ganancia financiera"
					not null
					default (0.00)
					,
	rotacion	num(5,1)	descr "Rotacion de la partida (dias)"
					,
	mdate	date	descr "Fecha de modificacion"
					,
}
primary key	(codadm, tipconf, nroconf);
grant all on partidas to public with grant option;

table vtaxart descr "Sumatoria de las ventas por articulo"
{
	codadm	char(8)	descr "Código de administración"
					not null
					mask $codadm
					,
	venta	num(6)	descr "Cantidad vendida en el periodo"
					not null
					default (0)
					,
}
primary key	(codadm);
grant all on vtaxart to public with grant option;

table impcvta descr "Imputaciones Contables de Ventas"
{
	tipcomp	num(4)	descr "Tipo de Comprobante"
					not null
					,
	concepto	num(2)	descr "Número de Concepto"
					not null
					,
	nroimp	num(2)	descr "Número de Imputación"
					not null
					,
	cctos	bool	descr "Afecta a Centro de Costos?"
					not null
					default (0)
					,
	div	num(3)	descr "Divisisn a la que afecta"
					,
	depto	num(4)	descr "Departamento al que afecta"
					,
	tipo	num(2)	descr "Ingreso o Egreso"
					in (1:"Ingreso", 2:"Egreso"),
	movini	bool	descr "Se carga por Mov. Inicio"
					not null
					default (0)
					,
	movcaja	bool	descr "Se carga por Mov. Caja"
					not null
					default (0)
					,
	cobranz	bool	descr "Se carga por la Cobranza"
					not null
					default (0)
					,
	movcie	bool	descr "Se carga por Mov. Cierre"
					not null
					default (0)
					,
	moneda	num(2)	descr "Tipo de Moneda"
					not null
					in moneda:descor,
	graba1	bool	descr "Graba 1 en arqueo"
					not null
					default (1)
					,
	concegr	num(2)	descr "Número de Concepto"
					,
	nimpegr	num(2)	descr "Número de Imputación"
					,
	movger	bool	descr "Se carga por Mov. de Gerente"
					not null
					default (0)
					,
	ingref	bool	descr "Ingresa referencia"
					not null
					default (0)
					,
	difcam	bool	descr "Es para Diferencia de Cambio"
					not null
					default (0)
					,
	grpprec	num(2)	descr "Grupo de Precinto"
					,
}
primary key	(tipcomp, concepto, nroimp),
index egreso	(tipcomp, concegr not null, nimpegr not null),
index difcambio	(tipcomp, difcam),
index grpprec	(tipcomp, grpprec not null);
grant all on impcvta to public with grant option;

table cf_cie_z descr "Cierres Z de Cajas"
{
	codimpr	num(4)	descr "Código de Dispositivo"
					not null
					in cfimpre:descrip,
	fecha	date	descr "Fecha del Cierre"
					not null
					default (today)
					,
	nroz	num(4)	descr "Nro. de Informe Z"
					not null
					,
	docfcanc	num(5)	descr "Cantidad de Doc. Fiscales Cancelados"
					not null
					default (0)
					,
	docnfh	num(5)	descr "Cantidad de Doc. NO Fiscales Homologados"
					not null
					default (0)
					,
	docnf	num(5)	descr "Cantidad de Doc. NO Fiscales"
					not null
					default (0)
					,
	docfemit	num(5)	descr "Cantidad de Doc. Fiscales Emitidos"
					not null
					default (0)
					,
	reservado	num(1)	descr "Reservado"
					,
	ultbc	num(8)	descr "Nro. ultimo doc. B/C Emitido"
					not null
					default (0)
					,
	ulta	num(8)	descr "Nro. ultimo doc. A Emitido"
					not null
					default (0)
					,
	vendidof	num(12,2)	descr "Monto Vendido en Doc. Fiscales"
					not null
					default (0.00)
					,
	ivaf	num(12,2)	descr "Monto IVA en Doc. Fiscales"
					not null
					default (0.00)
					,
	impintf	num(12,2)	descr "Monto Impuestos Internos Doc. Fiscales"
					not null
					default (0.00)
					,
	percepf	num(12,2)	descr "Monto Percepciones en Doc. Fiscales"
					not null
					default (0.00)
					,
	ivanoif	num(12,2)	descr "Monto IVA NO Inscripto en Doc. Fiscales"
					not null
					default (0.00)
					,
	ultnca	num(8)	descr "Nro. ultimo doc. A Emitido"
					not null
					default (0)
					,
	ultncbc	num(8)	descr "Nro. ultimo doc. B/C Emitido"
					not null
					default (0)
					,
	crednc	num(12,2)	descr "Creditos en NC"
					not null
					default (0.00)
					,
	ivanc	num(12,2)	descr "IVA en NC"
					not null
					default (0.00)
					,
	impintnc	num(12,2)	descr "Impuestos Internos en NC"
					not null
					default (0.00)
					,
	percepnc	num(12,2)	descr "Percepciones en NC"
					not null
					default (0.00)
					,
	ivanoinc	num(12,2)	descr "IVA No inscripto en NC"
					not null
					default (0.00)
					,
	ultrem	num(8)	descr "Nro. Ultimo Remito"
					not null
					default (0)
					,
}
primary key	(codimpr, nroz) [ 11 ],
unique index fecha	(codimpr, fecha, nroz) [ 14 ];
grant all on cf_cie_z to public with grant option;

table cf_perc_z descr "PERCEPCIONES Cierres Z de Cajas"
{
	codimpr	num(4)	descr "Código de Dispositivo"
					not null
					in cfimpre:descrip,
	nroz	num(4)	descr "Nro. de Informe Z"
					not null
					,
	renglon	num(2)	descr "Renglon de la Percepcion"
					not null
					default (1)
					,
	porciva	char(5)	descr "Porcentaje"
					not null
					,
	monto	num(12,2)	descr "Monto"
					not null
					,
	tipdoc	num(1)	descr "Tipo de Documento"
					not null
					default (2)
					in (2:"Documentos Fiscales", 4:"Notas de credito"),
}
primary key	(codimpr, nroz, renglon) [ 9 ];
grant all on cf_perc_z to public with grant option;

table cf_iva_z descr "IVA Cierres Z de Cajas"
{
	codimpr	num(4)	descr "Código de Dispositivo"
					not null
					in cfimpre:descrip,
	nroz	num(4)	descr "Nro. de Informe Z"
					not null
					,
	renglon	num(2)	descr "Renglon de la Percepcion"
					not null
					default (1)
					,
	porciva	char(5)	descr "Porcentaje"
					not null
					,
	monto	num(12,2)	descr "Monto"
					not null
					,
	montoii	num(12,2)	descr "Monto Impuestos Internos"
					not null
					,
	montonoi	num(12,2)	descr "Monto Responsable No inscripto"
					not null
					,
	neta	num(12,2)	descr "Monto Neto Sin IVA"
					not null
					,
	tipdoc	num(1)	descr "Tipo de Documento"
					not null
					default (1)
					in (1:"Documentos Fiscales", 3:"Notas de credito"),
}
primary key	(codimpr, nroz, renglon) [ 22 ];
grant all on cf_iva_z to public with grant option;

table cfcbte descr "Comprobantes por Controlador"
{
	tipomov	num(2)	descr "Tipo de Movimiento"
					not null
					,
	numcomp	num(8)	descr "Número de Comprobante"
					not null
					,
	nrocorr	num(2)	descr "Nro. Correlativo"
					not null
					default (0)
					,
	codimpr	num(4)	descr "Código de Dispositivo"
					not null
					in cfimpre:descrip,
	fecha	date	descr "Fecha del Movimiento"
					not null
					,
	letracf	char(2)	descr "Letra Controlador"
					not null
					,
	tipofac	char(1)	descr "Tipo de Factura"
					,
	seriefac	char(4)	descr "Sucursal Nro."
					mask "4n"
					,
	nrofac	char(8)	descr "Factura Nro."
					mask "8n"
					,
	items	num(5)	descr "Cantidad de Doc. Fiscales Emitidos"
					not null
					default (0)
					,
	vendido	num(12,2)	descr "Monto Vendido"
					not null
					default (0.00)
					,
	pagado	num(12,2)	descr "Monto Pagado"
					not null
					default (0.00)
					,
	iva	num(12,2)	descr "Monto IVA"
					not null
					default (0.00)
					,
	ivanoi	num(12,2)	descr "Monto IVA NO Inscripto"
					not null
					default (0.00)
					,
	impint	num(12,2)	descr "Monto Impuestos Internos"
					not null
					default (0.00)
					,
	nroz	num(4)	descr "Nro. de Informe Z"
					not null
					default (1)
					,
}
primary key	(tipomov, numcomp, nrocorr) [ 55 ],
unique index tipofac	(tipofac not null, seriefac not null, nrofac not null, tipomov, numcomp, nrocorr) [ 55 ],
unique index nroz	(nroz, tipomov, numcomp, nrocorr) [ 55 ],
unique index codimpr	(codimpr, fecha, nroz, tipomov, numcomp, nrocorr) [ 55 ];
grant all on cfcbte to public with grant option;

table cfcbteper descr "Ivas de Comprobantes por Controlador"
{
	tipomov	num(2)	descr "Tipo de Movimiento"
					not null
					,
	numcomp	num(8)	descr "Número de Comprobante"
					not null
					,
	nrocorr	num(2)	descr "Nro. Correlativo"
					not null
					default (0)
					,
	renglon	num(2)	descr "Renglon de la Percepcion"
					not null
					default (1)
					,
	porciva	char(5)	descr "Porcentaje"
					not null
					,
	monto	num(12,2)	descr "Monto"
					not null
					,
	tipdoc	num(1)	descr "Tipo de Documento"
					not null
					default (2)
					in (2:"Documentos Fiscales", 4:"Notas de credito"),
	sobret	num(1)	descr "Sobre total"
					not null
					default (0)
					in (0:"Indistinto", 1:"Ingresos Brutos", 2:"IVA 3337"),
}
primary key	(tipomov, numcomp, nrocorr, renglon) [ 15 ];
grant all on cfcbteper to public with grant option;

table cfcbteiva descr "Ivas de Comprobantes por Controlador"
{
	tipomov	num(2)	descr "Tipo de Movimiento"
					not null
					,
	numcomp	num(8)	descr "Número de Comprobante"
					not null
					,
	nrocorr	num(2)	descr "Nro. Correlativo"
					not null
					default (0)
					,
	renglon	num(2)	descr "Renglon de la Percepcion"
					not null
					default (1)
					,
	porciva	char(5)	descr "Porcentaje"
					not null
					,
	monto	num(12,2)	descr "Monto"
					not null
					,
	montoii	num(12,2)	descr "Monto Impuestos Internos"
					not null
					,
	montonoi	num(12,2)	descr "Monto Responsable No inscripto"
					not null
					,
	neta	num(12,2)	descr "Monto Neto Sin IVA"
					not null
					,
	tipdoc	num(1)	descr "Tipo de Documento"
					not null
					default (1)
					in (1:"Documentos Fiscales", 3:"Notas de credito"),
}
primary key	(tipomov, numcomp, nrocorr, renglon) [ 55 ];
grant all on cfcbteiva to public with grant option;

table relcotiz descr "Relaciones entre Cotizaciones (Aurus & Ventas)"
{
	tipcot	num(2)	descr "Tipo de Cotización de Aurus"
					not null
					,
	codmon	num(2)	descr "Código de Moneda"
					not null
					in moneda:descrip,
	tipo	num(2)	descr "Tipo de Cotizacion"
					not null
					,
}
primary key	(tipcot);
grant all on relcotiz to public with grant option;

table gcotmon descr "Cotizaciones diarias por moneda-tipo"
{
	codmon	num(2)	descr "Código de Moneda"
					not null
					in moneda:descrip,
	tcotiz	num(2)	descr "Tipo de Cotizacion"
					not null
					,
	fecha	date	descr "Fecha de Validez de la Cotización Desde"
					not null
					,
	hora	time	descr "Hora de Validez de la Cotización Desde"
					not null
					,
	valor	float	descr "Valor de La Cotización"
					not null
					,
	cdate	date	descr "Fecha de Carga"
					,
	ctime	time	descr "Hora de Carga"
					,
	cuid	num(5)	descr "Usuario que Cargo"
					,
}
primary key	(codmon, tcotiz, fecha, hora);
grant all on gcotmon to public with grant option;

table bloqtran descr "Bloqueo del Transportista para Valoriz.Flete"
{
	trans	num(9)	descr "Transportista bloqueado"
					,
}
primary key	(trans);
grant all on bloqtran to public with grant option;

table minfleh descr "Minimos de Flete Horarios"
{
	suc	num(3)	descr "Sucursal"
					not null
					,
	zona	char(1)	descr "Codigo de zona"
					not null
					mask "1>A"
					,
	cantidad	num(9)	descr "Cantidad"
					not null
					
					check (this >= 0),
	fdesde	date	descr "Fecha desde"
					not null
					,
	hdesde	time	descr "Hora desde"
					not null
					,
	minflete	num(10,2)	descr "Valor"
					not null
					,
	emp	num(3)	descr "Empresa"
					,
	nroleg	num(9)	descr "Nro. de Legajo"
					,
	cdate	date	descr "Fecha de Carga"
					,
	ctime	time	descr "Hora de Carga"
					,
	cuid	num(5)	descr "Usuario que Cargo"
					,
	mdate	date	descr "Fecha de Modificacion"
					,
	mtime	time	descr "Hora de Modificacion"
					,
	muid	num(5)	descr "Usuario que Modifico"
					,
}
primary key	(suc, zona, cantidad, fdesde, hdesde) [ 10 ],
index fecha	(suc, fdesde, hdesde, zona, cantidad) [ 10 ];
grant all on minfleh to public with grant option;

table prefacth descr "Tabla Modificaciones prefactura"
{
	prefa	num(8)	descr "Hoja de Ruta"
					not null
					,
	nroconf	num(8)	descr "Numero de Confirme"
					not null
					,
	renglon	num(3)	descr "Renglon Prefactura"
					,
	tipreng	char(1)	descr "Tipo de Renglon Manual o Automatico"
					,
	prov	char(1)	descr "Provincia Destino"
					,
	local	num(5)	descr "Localidad Destino"
					,
	area	num(3)	descr "Codigo de area"
					,
	direcc	char(45)	descr "Direccion Destino"
					,
	modif	char(1)	descr "Tipo de Modificación"
					in ("A":"ALTA", "B":"BAJA", "M":"MODIFICACION"),
	usrid	num(5)	descr "Identificación del Usuario"
					,
	fecha	date	descr "Fecha de Modificación"
					,
	hora	time	descr "Hora de Modificación"
					,
}
primary key	(prefa, nroconf, renglon, fecha, hora) [ 25 ],
index fecha	(fecha, prefa, nroconf, renglon) [ 22 ];
grant all on prefacth to public with grant option;

table areasxloh descr "Areas por Provincia y Localidad Historico"
{
	deposito	num(3)	descr "Depósito origen"
					not null
					,
	codigo	num(3)	descr "Codigo de area"
					not null
					,
	prov	char(1)	descr "Provincia"
					not null
					mask "1>x"
					,
	local	num(5)	descr "Código de Localidad"
					,
	usrid	num(5)	descr "Identificación del Usuario"
					,
	fecha	date	descr "Fecha de Modificación"
					,
	hora	time	descr "Hora de Modificación"
					,
	modif	char(1)	descr "Tipo de Modificación"
					in ("A":"ALTA", "B":"BAJA", "M":"MODIFICACION"),
}
primary key	(deposito, codigo, prov, local, fecha, hora),
index fecha	(deposito, codigo, fecha, hora),
unique index prov	(deposito, prov, local, fecha, hora);
grant all on areasxloh to public with grant option;

table tarxtrah descr "Relacion Tarifa-Trasportista Historico"
{
	deposito	num(3)	descr "Depósito origen de Hoja de Ruta"
					not null
					,
	trans	num(9)	descr "Transportista en AURUS"
					not null
					,
	codigo	num(3)	descr "Código de Tarifa"
					not null
					,
	tipo	char(1)	descr "Tarifa especial o normal"
					not null
					default ("N")
					mask ">A"
					in ("E":"Especial", "N":"Normal"),
	usrid	num(5)	descr "Identificación del Usuario"
					,
	fecha	date	descr "Fecha de Modificación"
					,
	hora	time	descr "Hora de Modificación"
					,
	modif	char(1)	descr "Tipo de Modificación"
					in ("A":"ALTA", "B":"BAJA", "M":"MODIFICACION"),
}
primary key	(deposito, codigo, trans, fecha, hora),
unique index transp	(deposito, trans, tipo, fecha, hora),
index fecha	(deposito, fecha);
grant all on tarxtrah to public with grant option;

table tarxdesh descr "Tarifas por destino Historico"
{
	deposito	num(3)	descr "Depósito origen de Hoja de Ruta"
					not null
					,
	codigo	num(3)	descr "Código de Tarifa"
					not null
					,
	codflia	char(8)	descr "Familia"
					mask $codflia
					,
	area	num(3)	descr "Area"
					in areas(deposito):descrip,
	mentrega	char(1)	descr "Modo de entrega"
					not null
					mask ">A"
					in ("D":"Domiciliarias", "E":"Express", "T":"Venta telefonica interior"),
	pribulto	num(6,2)	descr "Importe del primer bulto"
					,
	incbulto	num(6,2)	descr "Importe incremento por bulto"
					,
	usrid	num(5)	descr "Identificación del Usuario"
					,
	fecha	date	descr "Fecha de Modificación"
					,
	hora	time	descr "Hora de Modificación"
					,
	modif	char(1)	descr "Tipo de Modificación"
					in ("A":"ALTA", "B":"BAJA", "M":"MODIFICACION"),
}
primary key	(deposito, codigo, mentrega, area, codflia, fecha, hora),
index fecha	(deposito, fecha);
grant all on tarxdesh to public with grant option;

table prexcanh descr "Precio del flete por cantidad de bultos Historico"
{
	deposito	num(3)	descr "Depósito origen de Hoja de Ruta"
					not null
					,
	codigo	num(3)	descr "Código de Tarifa"
					not null
					,
	codflia	char(8)	descr "Familia"
					mask $codflia
					,
	area	num(3)	descr "Area"
					in areas(deposito):descrip,
	mentrega	char(1)	descr "Modo de entrega"
					not null
					mask ">A"
					in ("D":"Domiciliarias", "E":"Express", "T":"Venta telefonica interior"),
	bultos	num(2)	descr "Cant. de bultos"
					,
	bulfac	num(2)	descr "Cant. de bultos a facturar"
					,
	usrid	num(5)	descr "Identificación del Usuario"
					,
	fecha	date	descr "Fecha de Modificación"
					,
	hora	time	descr "Hora de Modificación"
					,
	modif	char(1)	descr "Tipo de Modificación"
					in ("A":"ALTA", "B":"BAJA", "M":"MODIFICACION"),
}
primary key	(deposito, codigo, mentrega, area, codflia, bultos, fecha, hora),
index fecha	(deposito, fecha, hora),
index areafec	(deposito, codigo, mentrega, area, codflia, fecha, hora);
grant all on prexcanh to public with grant option;

table plandep descr "Planillas de Depositos por Medios de Pago"
{
	nroplan	num(8)	descr "Nro. de Planilla"
					not null
					,
	sucur	num(3)	descr "Nro. de Sucursal"
					not null
					,
	mediop	num(2)	descr "Medio de Pago"
					not null
					,
	submed	num(3)	descr "Sub-Medio de Pago"
					not null
					,
	estado	char(1)	descr "Estado"
					default ("A")
					mask ">A"
					in ("A":"Alta", "P":"Provisorio", "D":"Definitivo", "C":"Cerrado"),
	fcie	date	descr "Fecha de Cierre"
					,
	fape	date	descr "Fecha de Apertura"
					not null
					default (today)
					,
	totsob	num(4)	descr "Cantidad Total de Sobres"
					not null
					default (0)
					,
	empg	num(3)	descr "Empresa Gerente"
					,
	nrolegg	num(9)	descr "Nro. de Legajo Gerente"
					,
	tipobs	num(4)	descr "Tipo de Observación"
					,
	codobs	num(8)	descr "Numero de Observacion"
					,
	empo	num(3)	descr "Empresa Oficial"
					,
	nrolego	num(9)	descr "Nro. de Legajo Oficial"
					,
	nrorem	num(9)	descr "Nro. Remito asociado"
					,
}
primary key	(nroplan) [ 5 ],
unique index sucur	(sucur, nroplan) [ 5 ],
unique index medios	(sucur, mediop, submed, nroplan) [ 5 ],
unique index estado	(estado, sucur, mediop, submed, nroplan) [ 5 ],
unique index fcie	(sucur, fcie not null, nroplan) [ 5 ],
unique index fape	(sucur, fape, nroplan) [ 5 ];
grant all on plandep to public with grant option;

table sobrdep descr "Sobres de Planillas de Depositos por Medios de Pago"
{
	nroplan	num(8)	descr "Nro. de Planilla"
					not null
					,
	nrosobr	num(4)	descr "Nro. de Sobre"
					not null
					,
	importe	num(11,2)	descr "Importe"
					not null
					,
	fecha	date	descr "Fecha"
					not null
					default (today)
					,
	hora	time	descr "Hora"
					not null
					default (hour)
					,
	fvta	date	descr "Fecha de Venta Real"
					not null
					default (today)
					,
	estado	char(1)	descr "Estado"
					default ("P")
					mask ">A"
					in ("P":"Pendiente", "R":"Rechazado", "A":"Aprobado", "F":"Faltante", "C":"Cerrado"),
	empc	num(3)	descr "Empresa Cajero"
					,
	nrolegc	num(9)	descr "Nro. de Legajo Cajero"
					,
	empg	num(3)	descr "Empresa Gerente"
					,
	nrolegg	num(9)	descr "Nro. de Legajo Gerente"
					,
	tipobs	num(4)	descr "Tipo de Observación"
					,
	codobs	num(8)	descr "Numero de Observacion"
					,
	cajero	num(5)	descr "Cajero Asociado"
					,
	codobsg	num(8)	descr "Numero de Observacion Gerencial"
					,
	sucur	num(3)	descr "Nro. de Sucursal"
					,
	origen	num(3)	descr "Origen del Sobre"
					,
	destino	num(3)	descr "Detino del Sobre"
					,
}
primary key	(nroplan, nrosobr) [ 20 ],
index caja	(nroplan, fecha, cajero not null) [ 27 ],
index sucur	(sucur not null, fecha, cajero, estado) [ 27 ];
grant all on sobrdep to public with grant option;

table hrutah descr "Historico de Hoja de Ruta"
{
	nrohoja	num(8)	descr "Hoja de Ruta"
					,
	trans	num(9)	descr "Número del Transportista"
					,
	seguro	num(9)	descr "Compañia de Seguro"
					,
	fletero	char(20)	descr "Fletero"
					,
	jaula	char(6)	descr "Nro. de Jaula"
					,
	comfle	num(2)	descr "Medio de Comunicacion del Fletero"
					,
	ncomfle	char(15)	descr "Numero de Medio de Comunicacion del Fletero"
					,
	mentrega	char(1)	descr "Modo de entrega"
					mask ">A"
					in ("D":"Domiciliarias", "E":"Express", "T":"Venta telefonica interior", "N":"No valorizada"),
	usrid	num(5)	descr "Identificación del Usuario"
					,
	fecha	date	descr "Fecha de Modificación"
					,
	hora	time	descr "Hora de Modificación"
					,
	modif	char(1)	descr "Tipo de Modificación"
					in ("A":"ALTA", "B":"BAJA", "M":"MODIFICACION"),
	reimp	bool	descr "Reimprimio?"
					not null
					default (0)
					,
	acompan	char(20)	descr "Acompaniante del Fletero"
					,
	patente	char(8)	descr "Patente del Flete"
					,
	despacho	char(20)	descr "Despacho"
					,
	fecdes	date	descr "Fecha de despacho."
					,
	ctrl	char(20)	descr "Persona que carga el camión"
					,
	sucdest	num(3)	descr "Sucursal de destino"
					,
	estado	char(1)	descr "Estado de la Hoja de Ruta"
					in ("P":"Provisoria", "C":"Cerrada", "D":"Definitiva"),
}
primary key	(nrohoja, fecha, hora) [ 17 ],
index fecha	(fecha, nrohoja) [ 14 ],
index reimp	(reimp, fecha) [ 10 ];
grant all on hrutah to public with grant option;

table porxusu descr "Porcentaje de Cambio de Neto por Usuario"
{
	codusu	num(5)	descr "Identificación del Usuario "
					not null
					,
	porcam	num(5,2)	descr "Porcentaje de Cambio"
					not null
					,
}
primary key	(codusu);
grant all on porxusu to public with grant option;

table ingxser descr "Ingresos de Comprobantes por Serie"
{
	tipomov	num(2)	descr "Tipo de Movimiento"
					not null
					,
	numcomp	num(8)	descr "Número de Comprobante"
					not null
					,
	codcta	num(8)	descr "Código de Cuenta"
					not null
					
					check (this > 0),
	codadm	char(8)	descr "Codigo de administración"
					,
	nroserie	char(20)	descr "Numero de Serie"
					,
	importe	num(11,2)	descr "Precio"
					,
	falta	date	descr "Fecha de Alta"
					not null
					default (today)
					,
	faprob	date	descr "Fecha Aprobacion"
					,
	estado	char(1)	descr "Estado"
					default ("P")
					mask ">A"
					in ("P":"Pendiente", "A":"Aprobado", "R":"Rechazado", "S":"Stock", "I":"Incorporado"),
	emp	num(3)	descr "Empresa Autorizante"
					,
	nroleg	num(9)	descr "Nro. de Legajo Autorizante"
					,
	senia	num(8)	descr "Nro. de Senia"
					,
	cajero	num(5)	descr "Cajero"
					,
	transfer	num(8)	descr "Transferencia"
					,
	sucur	num(3)	descr "Sucursal"
					,
	marca	num(2)	descr "Marca"
					,
	codrech	num(2)	descr "Codigo de Rechazo"
					,
	cdate	date	descr "Fecha de Carga"
					,
	ctime	time	descr "Hora de Carga"
					,
	cuid	num(5)	descr "Usuario que Cargo"
					,
	mdate	date	descr "Fecha de Modificacion"
					,
	mtime	time	descr "Hora de Modificacion"
					,
	muid	num(5)	descr "Usuario que Modifico"
					,
}
primary key	(tipomov, numcomp),
unique index serie	(tipomov, nroserie),
unique index cajero	(tipomov, estado, sucur, cajero, numcomp),
unique index senia	(tipomov, senia, numcomp),
unique index sucur	(tipomov, sucur, numcomp);
grant all on ingxser to public with grant option;

table movserie descr "Movimientos por Número de Serie"
{
	tipomov	num(2)	descr "Tipo de Movimiento"
					not null
					,
	numcomp	num(8)	descr "Número de Comprobante"
					not null
					,
	renglon	num(2)	descr "Renglón"
					not null
					,
	nrosec	num(2)	descr "Número de Secuencia"
					not null
					default (0)
					,
	nroserie	char(20)	descr "Numero de Serie"
					not null
					,
	nvta	num(8)	descr "Nota de Venta Relacionada"
					,
}
primary key	(tipomov, numcomp, renglon, nrosec);
grant all on movserie to public with grant option;

table logserv descr "Log Transferencias Service Propio"
{
	tipomov	num(2)	descr "Tipo de Movimiento"
					,
	numcomp	num(8)	descr "Número de Comprobante"
					,
	numtrans	num(8)	descr "Número de Comprobante sistema remoto"
					,
	codadm	char(8)	descr "Código de administración"
					,
	renglon	num(2)	descr "Renglón"
					,
	nrosec	num(2)	descr "Número de secuencia"
					default (0)
					,
	nroserie	char(20)	descr "Número de Serie"
					,
	fecha	date	descr "Fecha Comprobante"
					,
	hora	time	descr "Hora Comprobante"
					,
	codigo	num(2)	descr "Código de Log"
					,
	descrip	char(40)	descr "Descripción de Log"
					,
	cdate	date	descr "Fecha de Carga"
					,
	ctime	time	descr "Hora de Carga"
					,
	cuid	num(5)	descr "Usuario que Cargo"
					,
	mdate	date	descr "Fecha de Modificación"
					,
	mtime	time	descr "Hora de Modificación"
					,
	muid	num(5)	descr "Usuario que Modifico"
					,
}
primary key	(tipomov, numcomp, numtrans, codadm, nroserie),
unique index numtrans	(tipomov, numtrans not null, codadm, nroserie, numcomp);
grant all on logserv to public with grant option;

table tarfleth descr "Tarifas de los fletes Historicos"
{
	deposito	num(3)	descr "Depósito origen de Hoja de Ruta"
					not null
					,
	codigo	num(3)	descr "Código de Tarifa"
					not null
					,
	descrip	char(40)	descr "Descripcion"
					not null
					,
	usrid	num(5)	descr "Identificación del Usuario"
					,
	fecha	date	descr "Fecha de Modificación"
					,
	hora	time	descr "Hora de Modificación"
					,
	modif	char(1)	descr "Tipo de Modificación"
					in ("A":"ALTA", "B":"BAJA", "M":"MODIFICACION"),
}
primary key	(deposito, codigo, fecha, hora),
index fecha	(deposito, fecha, codigo);
grant all on tarfleth to public with grant option;

table fliasvah descr "Familias sin Valorizacion de Fletes Historico"
{
	codflia	char(8)	descr "Código de familia"
					not null
					,
	usrid	num(5)	descr "Identificación del Usuario"
					,
	fecha	date	descr "Fecha de Modificación"
					,
	hora	time	descr "Hora de Modificación"
					,
	modif	char(1)	descr "Tipo de Modificación"
					in ("A":"ALTA", "B":"BAJA", "M":"MODIFICACION"),
}
primary key	(codflia, fecha, hora),
index fecha	(fecha, codflia);
grant all on fliasvah to public with grant option;

table motcarh descr "Motivos de cargos a fletes Historico"
{
	motivo	num(2)	descr "Motivo del cargo"
					not null
					,
	descrip	char(40)	descr "Descripcion"
					not null
					,
	signo	num(1)	descr "Signo del cargo"
					not null
					in (1:"Positivo", -1:"Negativo"),
	maximo	num(7,2)	descr "Importe maximo"
					,
	minimo	num(7,2)	descr "Importe minimo"
					,
	usrid	num(5)	descr "Identificación del Usuario"
					,
	fecha	date	descr "Fecha de Modificación"
					,
	hora	time	descr "Hora de Modificación"
					,
	modif	char(1)	descr "Tipo de Modificación"
					in ("A":"ALTA", "B":"BAJA", "M":"MODIFICACION"),
}
primary key	(motivo, fecha, hora),
index fecha	(fecha, motivo);
grant all on motcarh to public with grant option;

table motmanh descr "Motivos Manuales de fletes Historico"
{
	cmotman	num(2)	descr "Motivo Manual"
					not null
					,
	descrip	char(40)	descr "Descripcion"
					not null
					,
	asocia	bool	descr "Se asocia a nvi/nci?"
					not null
					,
	maximo	num(7,2)	descr "Importe maximo"
					,
	minimo	num(7,2)	descr "Importe minimo"
					,
	ctacble	num(6)	descr "Cuenta contable"
					,
	ctaalt	num(6)	descr "Cuenta contable Alternativa"
					,
	usrid	num(5)	descr "Identificación del Usuario"
					,
	fecha	date	descr "Fecha de Modificación"
					,
	hora	time	descr "Hora de Modificación"
					,
	modif	char(1)	descr "Tipo de Modificación"
					in ("A":"ALTA", "B":"BAJA", "M":"MODIFICACION"),
}
primary key	(cmotman, fecha, hora),
index fecha	(fecha, cmotman);
grant all on motmanh to public with grant option;

table cflru descr "Motivos Manuales de fletes Historico"
{
	tipomov	num(2)	descr "Tipo de movimiento"
					not null
					,
	numcomp	num(8)	descr "Nro. de comprobante"
					not null
					,
	tipodoc	char(1)	descr "Tipo de Factura"
					not null
					,
	seriedoc	num(4)	descr "Serie de Factura"
					not null
					,
	nrodoc	num(8)	descr "Nro. de Factura"
					not null
					,
	suc	num(3)	descr "Sucursal"
					,
	fecha	date	descr "Fecha de Alta"
					,
}
primary key	(tipomov, numcomp),
index doc	(tipodoc, seriedoc, nrodoc);
grant all on cflru to public with grant option;

table confsob descr "Configuracion de Sobres"
{
	mediop	num(2)	descr "Medio de Pago"
					not null
					,
	submed	num(3)	descr "Sub-Medio de Pago"
					not null
					,
	origen	num(3)	descr "Origen del sobre"
					not null
					,
	destino	num(3)	descr "Destino del sobre"
					not null
					,
	arqueo	bool	descr "Se concidera en el arqueo"
					,
	ctadeb	num(6)	descr "Cuenta contable Débito"
					,
	ctacred	num(6)	descr "Cuenta contable Crédito"
					,
}
primary key	(mediop, submed, origen, destino);
grant all on confsob to public with grant option;


grant alter on schema aux to public;
