/*
* MODULO y VERSION : %W%
* FECHA            : %D% %T%
*
* MODIFICACION     : %E% %U%
*/
/****************************************************************************/
/*                                                                          */
/* No Agregar Tablas a este SCHEMA, la idea es utilizar el schema vengral,  */
/* el cual por naturaleza esta compartido por Garbarino y Compumundo        */
/* Su origen es multi-empresa, por lo tanto en caso de ser necesario se     */
/* debera incluir en la tabla nueva el campo "nroemp"                       */
/*                                                                          */
/****************************************************************************/

schema serv					descr "Esquema de service de GARBARINO"
							language "C";

table SERVICES				descr "Services" {

	codserv		num(4)		descr "Código de service"
							not null,
	activo		bool		descr "Activo"
							default false not null,
	razsoc		char(50)	descr "Razon Social - Nombre"
							mask "50>x" not null,
	descor		char(15)	descr "Descripcion abreviada"
							mask "15>x" not null,
	direcc		char(50)	descr "Calle y Número"
							mask "50>x" not null,
	local		char(30)	descr "Localidad"
							mask "30>x" not null,
	prov		char(1)		descr "Provincia"
							mask "1>x" not null,
	cpostal		num(5)		descr "Código Postal",
	area		char(5)		descr "Telediscado"
							not null,
	telefono	char(8)		descr "Número de Teléfono"
							not null,
}
primary key (codserv);

table SERVMARCA				descr "Relacion Services - Marca" {

	codserv		num(4)		descr "Código de service"
							in services : razsoc
							not null,
	marca		num(8)		descr "Código de marca"
							// in ventas.MARCA : descrip
							not null,
}
primary key (codserv, marca),
unique index MARCA (marca, codserv);

table GRUPTCAT				descr "Tipo de grupos de articulos" {

	codtipo		num(2)		descr "Código de tipo de grupo"
							not null,
	descrip		char(30)	descr "Descripcion"
							not null,
	descor		char(10)	descr "Descripcion abreviada"
							not null,
	unigrupo	bool		descr "El articulo debe estar en un unico grupo ?"
							default true not null,
	asocia		bool		descr "Asocia grupo de artículos"
							default false not null,
	clase		num(2)		descr "Clase de tipo de Grupos",
							// in ventas.CODIFICA(284)
	archweb		num(3)		descr	"Tipo Archivo Web",
							// in ventas.CODIFICA(318)
}
primary key (codtipo),
unique index CLASE (clase not null, codtipo),
index WEB   (archweb not null, codtipo);

table GRUPCAT				descr "Grupos de articulos" {

	codtipo		num(2)		descr "Código de tipo de grupo"
							in gruptcat : descrip
							not null,
	codgrp		num(4)		descr "Código de grupo"
							not null,
	descrip		char(30)	descr "Descripcion"
							not null,
	descor		char(10)	descr "Descripcion abreviada"
							not null,
	repar		char		descr "Reparacion"
	  							in ("D" : "Domicilio o Flete sin cargo al Taller de Service más cercano",
								"T" : "Taller")
							mask ">a",
}
primary key (codtipo, codgrp);

table PERGAR				descr "Periodos de Garantias" {

	codper		num(4)		descr "Código de periodo"
							not null,
	descrip		char(30)	descr "Descripcion"
							not null,
	descor		char(10)	descr "Descripcion abreviada"
							not null,
	periodo		num(4)		descr "Periodo (meses)"
							>= 0 not null,
	impr		char(15)	descr "Descr. a imprimir"
							not null,
	poliza		num(6)		descr "Póliza"
							not null,
	tgarfab		num(4)		descr "Tiempo de gar. fabricante",
	serie		num(4)		descr "Serie"
							/*in ventas.series:(descor)*/,
	porctdo		num(4,2)	descr "Porc. de comis. ctdo."
							default 0
							not null,
	porcred		num(4,2)	descr "Porc. de comis. cred."
							default 0
							not null,
}
primary key (codper);
//unique index POLIZA(poliza);

table GRUPDCAT				descr "Detalle de Grupo de Art." {

	codtipo		num(2)		descr "Código de tipo de grupo"
							in gruptcat : descrip
							not null,
	codgrp		num(4)		descr "Código de grupo"
							in grupcat (codtipo) : descrip
							not null,
	codadm		char(8)		descr "Código de administración"
							// in ventas.CATALOGO:(des1adm)
							not null
							mask $codadm,
}
primary key (codtipo, codgrp, codadm),
unique index CODADM (codadm, codtipo, codgrp);

table GAREXT				descr "Garantias Extendidas" {

	poliza		num(6)		descr "Póliza"
							not null,
	numgar		num(8)		descr "Número de Póliza"
							not null,
	estado		num(2)		descr "Estado"
							// ventas.in CODIFICA (10) : descrip,
							not null,
}
primary key (poliza, numgar) [16]; /* Base 900.000 */

table RGARMOV				descr "Relacion Garantias Ext. - Movimientos" {

	tipomov		num(2)		descr "Tipo de Movimiento Relacionado"
							// ventas.in TIPOMOV:descrip,
							not null,
	numcomp		num(8)		descr "Número de Comprobante Relacionado"
							not null,
	renglon		num(3)		descr "Renglon Relacionado"
							in DATMOVGE (tipomov, numcomp) : (codgar, codadm)
							not null,
	poliza		num(6)		descr "Póliza"
							// de Ventas
							not null,
	numgar		num(8)		descr "Número de Póliza"
							// de Ventas
							not null
							in GAREXT(poliza) : estado,
}
primary key (tipomov, numcomp, renglon, poliza, numgar) [27],
unique index GARANTIA (tipomov, poliza, numgar) [18]; /* Base 1.000.000 */

table GARDEXT				descr "Detalle de Garantias Extendidas" {

	poliza		num(6)		descr "Póliza"
							not null,
	numgar		num(8)		descr "Número de Póliza"
							not null
							in GAREXT(poliza): estado,
	renglon		num(3)		descr "Renglon"
							not null,
	codserv		num(4)		descr "Código de service"
							in services : razsoc,
	fecha		date		descr "Fecha"
							default today not null,
	hora		time		descr "Hora"
							default hour not null,
	usuario		num(5)		descr "Usuario"
							not null,
	codobs		num(8)		descr "Numero de Observacion",
	estado		num(2)		descr "Estado"
							// ventas.in CODIFICA (10) : descrip,
							not null,
}
primary key (poliza, numgar, renglon);

table OBSGRAL				descr "Observ. Generales" {

	tipobs		num(4)		descr "Tipo de Observación"
							not null, //in ventas.sernum
	codobs		num(8)		descr "Numero de Observacion"
							not null,
	renglon		num(3)		descr "Renglon"
							not null,
	observ		char(60)	descr "Observacion"
							not null,
}
primary key (tipobs, codobs, renglon) [56] /* Base 20.000.000 */;

table CATGAREX				descr "Datos del catalogo de gar. ext." {

	codadm		char(8)		descr "Código de administración"
							// in ventas.CATALOGO:(des1adm) & tipo == 'G'
							not null
							mask $codadm,
	codper		num(4)		descr "Código de periodo"
							in pergar : (descor, periodo)
							not null,
}
primary key (codadm),
unique index CODPER (codper, codadm);

table DATGE					descr "Datos Compr. Gar. Ext./ CAT" {

	tipomov		num(2)		descr "Tipo de Movimiento Relacionado"
							// ventas.in TIPOMOV:descrip,
							not null,
	numcomp		num(8)		descr "Número de Comprobante Relacionado"
							not null,
	vendedor	num(4)		descr "Vendedor",
							// in ventas.VENDEDORES : vendnom
							// not null si es para Gar. Ext.
	sucursal	num(3)		descr "Sucursal donde se realizo la venta"
							// in ventas.SUCURSAL : descor
							not null,
	fefact		date		descr "Fecha de Facturacion"
							not null,
	discr		bool		descr "Discrimina Iva"
							not null default false,
	apynom		char(50)	descr "Apellido y Nombre / Empresa"
							mask "50>x"
							not null,
	direccion	char(30)	descr "Dirección del Cliente."
							not null,
	local		char(30)	descr "Localidad del Cliente.",
	prov		char(1)		descr "Provincia"
							// in ventas.PROVINCIAS : descrip
							not null
							mask ">x",
	cpostal		num(4)		descr "Codigo postal",
	cuit		char(15)	descr "C.u.i.t. del Cliente.",
	tipoiva		num(2)		descr "Tipo de Iva del Cliente.",
							// in ventas TIVA by VENTAS(true) : descrip
							// not null si es para Gar. Ext.
	tipdoc		char(3)		descr "Tipo de Documento del Cliente.",
	nrodoc		num(9)		descr "Nro. de Documento del Cliente.",
	area		char(5)		descr "Numero de DDN",
	telefono	char(8)		descr "Numero de telefono",
}
primary key (tipomov, numcomp) [12],
index APYNOM (apynom) [55];  /* Base 980.000 */

table DATMOVGE				descr "Datos Mov. Garantias Ext." {

	tipomov		num(2)		descr "Tipo de Movimiento Relacionado"
							// ventas.in TIPOMOV:descrip,
							not null,
	numcomp		num(8)		descr "Número de Comprobante Relacionado"
							in DATGE (tipomov) : (apynom)
							not null,
	renglon		num(3)		descr "Renglon Relacionado"
							// in ventas.MOVIM (tipomov, numcomp, codadm)
							not null,
	codgar		char(8)		descr "Garantia (articulo)"
							// ventas.in CATALOGO:(des1adm)
							not null
							mask $codadm,
	codadm		char(8)		descr "Articulo en garantia"
							// ventas.in CATALOGO:(des1adm)
							not null
							mask $codadm,
	cantidad	num(9)		descr "Cantidad de unidades"
							not null,
	tgarfab		num(4)		descr "Tiempo de gar. fabricante"
							not null,
	tgarext		num(4)		descr "Tiempo de gar. extendida"
							not null,
	codtipo		num(2)		descr "Código de tipo de grupo"
							in gruptcat : descrip
							not null,
	codgrp		num(4)		descr "Código de grupo"
							in grupcat (codtipo) : descrip
							not null,
	pcodadm		num(11,2)	descr "Precio (articulo)"
							not null,
	pcodgar		num(11,2)	descr "Precio gar. ext.(c/IVA)"
							not null,
	iva			num(11,2)	descr "Valor de IVA"
							not null,
	codper		num(4)		descr "Código de periodo"
							in pergar : (descor, periodo)
							not null,
 	tiprel		num(2)		descr "Tipo de Movimiento Relacionado",
	numrel		num(8)		descr "Número de Movimiento Relacionado",
	fecvig  	date		descr "Fecha de Vigencia"
							not null,
	repar		char		descr "Reparacion"
							// idem GRUPCAT.repar
							mask ">a",
	existe		num(2)		descr "Existe ?",
							// in ventas.CODIFICA (44)
	tipogar		char(1)		descr "Tipo de Gar. Ext"
							in ("G": "Gar.Ext",
								"R": "Renov de gar.ext")
							not null
							default "G",
	codrenov	char(8)		descr "Articulo en garantia"
							// ventas.in CATALOGO:(des1adm)
							mask $codadm,
	renrel		num(3)		descr "Renglon del Movimiento relacionado",
}
primary key (tipomov, numcomp, renglon) [43],
index tiprel (tiprel not null, numrel not null, codadm) [66]; /* Base 7.973.191 */

table PROVCAT				descr "Proveedores de Artículos del Catálogo" {

	codadm		char(8)		descr "Código de Administración"
							not null,
							// in VENTAS.CATALOGO
	emp			num(2)		descr "Empresa"
							not null,
							// in AURUS.EMPS
	tipccte		num(2)		descr "Tipo de Cuenta Corriente del Proveedor"
							not null,
							// in AURUS.TCCTES
	nroccte		num(6)		descr "Número de Cuenta Corriente del Proveedor"
							not null,
							// in AURUS.CCTES
	activo		bool		descr "Está Activa la Relación p/Calculo de Comisión"
							not null default true,
}
primary key (emp, tipccte, nroccte, codadm),
unique index CODADM (codadm, emp, tipccte, nroccte);

table GRUPROV				descr "Tipos de Agrupamiento de Provincias" {

	codtipo		num(2)		descr "Código de Tipo de Agrupamiento"
							not null,
							// in ventas.CODIFICA : (47)
	grprov		num(2)		descr "Código de Agrupamiento"
							not null,
	descrip		char(30)	descr "Descripción"
							not null,
	descor		char(10)	descr "Descripción Abreviada"
							not null,
}
primary key(codtipo, grprov);

table GRUDPROV				descr "Detalle de Agrupamiento de Provincias" {

	codtipo		num(2)		descr "Código de Tipo de Agrupamiento"
							not null,
							// in ventas.CODIFICA : (47)
	grprov		num(2)		descr "Código de Agrupamiento"
							not null
							in gruprov(codtipo):descrip,
	prov		char(1)		descr "Provincia"
							// in ventas.PROVINCIAS : descrip
							not null
							mask ">x",
}
primary key(codtipo, grprov, prov),
unique index PROV(prov, codtipo, grprov);

table VALGE					descr "Valores de Garantía Extendida" {

	codper		num(4)		descr "Código de periodo"
							not null
							in pergar:(periodo),
	codtipo		num(2)		descr "Código de Tipo de Agrupamiento"
							not null,
	codgrp		num(4)		descr "Código de grupo"
							in grupcat (codtipo) : descrip
							not null,
	grprov		num(2)		descr "Agrupamiento de provincias"
							not null
						   	in gruprov(1) : descrip,
	presiva		num(11,2)	descr "Precio sin Iva"
							not null,
	iva			num(11,2)	descr "Iva"
							not null,
	prefin		num(11,2)	descr "Precio Final"
							not null,
}
primary key(codper,grprov,codtipo,codgrp),
unique index PERGRP (codper,codtipo,codgrp,grprov);

table PROVADIC				descr "Datos Adicionales de Proveedores" {

	emp			num(2)		descr "Empresa"
							not null,
							// in AURUS.EMPS
	tipccte		num(2)		descr "Tipo de Cuenta Corriente del Proveedor"
							not null,
							// in AURUS.TCCTES
	nroccte		num(6)		descr "Número de Cuenta Corriente del Proveedor"
							not null,
							// in AURUS.CCTES
	activo		bool		descr "Está Activa la Relación para FRU"
							not null default true,
	perrend		num(3)		descr "Periodo de Rendición (en días)",
	ultrend		date		descr "Última rendición"
}
primary key (emp, tipccte, nroccte);

table DATCAT				descr "Datos adic. Centro At. Cli. (CAT)" {

	tipomov		num(2)		descr "Tipo de Movimiento Relacionado"
							// ventas.in TIPOMOV:descrip,
							not null,
	numcomp		num(8)		descr "Número de Comprobante Relacionado"
							not null,
	renglon		num(3)		descr "Renglon Relacionado"
							// in ventas.MOVIM (tipomov, numcomp, codadm)
							not null,
	nvi			num(8)		descr "Numero de Nota de Venta",
	horario		char(20)	descr "Horario",
	nroserie	char(20)	descr "Numero de Serie"
							not null,
	nroparte	char(20)	descr "Numero de Parte"
							not null,
	senias		char(50)	descr "Senias",
	defecto		char(50)	descr "Defecto",
	recibe		char(50)	descr "Recibe con ...",
}
primary key (tipomov, numcomp, renglon);

table VALPROV				descr "Valores Resumen por Proveedor" {

	emp			num(2)		descr "Empresa"
							not null,
							// in AURUS.EMPS
	tipcie		num(2)		descr "Tipo de Cierre"
							not null,
							// in ventas.codifica(14)
	tipccte		num(2)		descr "Tipo de Cuenta Corriente del Proveedor"
							not null,
							// in AURUS.TCCTES
	fdesde		date		descr "Fecha Desde"
							not null,
	nroccte		num(6)		descr "Número de Cuenta Corriente del Proveedor"
							not null,
							// in AURUS.CCTES
	fhasta		date		descr "Fecha Hasta"
							not null,
	cant		num(9)      descr "Cantidad"
	                        not null default 0,
	val			num(12,2)   descr "Monto"
	                        not null default 0.0,
	tipcomp	    num(4)		descr "Tipo de Comprobante Asiento Relacionado",
	serie       num(4)		descr "Serie de Comprobante Asiento Relacionado",
	nrocomp	    num(9)		descr "Nro. de Comprobante Asiento Relacionado",
}
primary key (emp, tipcie, tipccte, fdesde, nroccte);


table VALSPROV				descr "Valores Resumen por Sucursal por Proveedor" {

	emp			num(2)		descr "Empresa"
							not null,
							// in AURUS.EMPS
	tipcie		num(2)		descr "Tipo de Cierre"
							not null,
							// in ventas.codifica(14)
	tipccte		num(2)		descr "Tipo de Cuenta Corriente del Proveedor"
							not null,
							// in AURUS.TCCTES
	fdesde		date		descr "Fecha Desde"
							not null,
	nroccte		num(6)		descr "Número de Cuenta Corriente del Proveedor"
							not null,
							// in AURUS.CCTES
	suc		    num(3)		descr "Sucursal Afectada"
							not null,
							// in ventas.SUCURSAL
	val			num(12,2)   descr "Monto"
	                        not null default 0.0,
}
primary key (emp, tipcie, tipccte, fdesde, nroccte, suc);

table CTADAT				descr "Datos de la Cuenta del Cliente" {

	codcta		num(8)		descr "Código de Cuenta"
							not null > 0,
	apynom		char(50)	descr "Apellido y Nombre / Empresa"
							mask "50>x"
							not null,
	tipdoc		char(3)		descr "Tipo de Documento"
							mask "3>x",
							// in gral.TIPDOC : descrip,
	nrodoc		num(8)		descr "Número de Documento",
	tipcta		num(2)		descr "Tipo de Cuenta"
							not null,
							// in ventas.CODIFICA(34):descrip,
	tipdir		num(2)		descr "Tipo de Dirección"
							not null,
							// in ventas.CODIFICA(35):descrip,
	prov		char(1)		descr "Provincia"
							mask "1>x"
							not null,
							// in ventas.PROVINCIAS:descrip
	local		num(5)		descr "Localidad de la Provincia"
							// in gral.LOCALPROV(prov):descrip
							not null,
	calle		num(4)		descr "Código de Calle",
							// in gral.CALLE(prov, local):descrip
	nro			num(5)		descr "Número",
	piso		char(2)		descr "Piso"
							mask "2>x",
	depto		char(4)		descr "Departamento"
							mask "4>x",
	ecalle1		num(4)		descr "Entre Primera Calle",
							// in gral.CALLE(prov, local):descrip
	ecalle2		num(4)		descr "Entre Segunda Calle",
							// in gral.CALLE(prov, local):descrip
	cpostal		num(4)		descr "Código Postal",
	fecnac		date		descr "Fecha de Nacimiento del Cliente",
	estcli		num(2)		descr "Estado del Cliente"
							not null,
							// in ventas.CODIFICA(36):descrip,
	tipoiva		num(2)		descr "Tipo de IVA"
							not null,
							// in ventas.TIVA
	cuit		char(15)	descr "C.U.I.T.",
	rg3337		bool		descr "Se le realiza RG3337"
							not null default false,
	suc			num(3)		descr "Sucursal de Alta"
							not null,
							// in ventas.SUCURSAL:descrip
	flag		num(2)		descr "Flag",
	vendedor	num(4)		descr "Vendedor",
							// in ventas.VENDEDORES : vendnom,
	dlocal		char(30)	descr "Descrip. localidad",
	dcalle		char(35)	descr "Descrip. calle",
	barrio		char(1)		descr "Barrio",
	partido		char(1)		descr "Partido",
	cpa			char(8)		descr "Codigo postal argentino",
	decalle1	char(35)	descr "Descrip. entre calle 1",
	decalle2	char(35)	descr "Descrip. entre calle 2",
	estdirec	num(2)		descr "Estado de validacion con Merlin",
	errdirec	num(2)		descr "Error de la validacion con Merlin"
							default 0,
	notocar		bool		descr "No se puede modif el cliente desde la NVTA"
							not null   default false,
	cdate		date		descr "Fecha de Alta",
	cuid		num(5)		descr "Usuario que dio el Alta",
	mdate		date		descr "Fecha de Modificación",
	muid		num(5)		descr "Usuario que Modificó",
	ctime		time		descr "Hora de Carga",
	mtime		time		descr "Hora de Modificacion",
}
primary key (codcta)[15],            // Base : 1.900.000
index APELLIDO (apynom(0,25))[55],
index TIPCTA (tipcta, apynom(0,25))[55],
index DOCUM (tipdoc not null, nrodoc not null)[21],
unique index CUIT (cuit not null)[34],
index merlin (estdirec not null)[10];

table CTADIR				descr "Direcciones por Cuenta" {

	codcta		num(8)		descr "Código de Cuenta Asociado"
							not null
							in CTADAT:apynom,
	coddir		num(4)		descr "Código de Dirección"
							not null,
	tipdir		num(2)		descr "Tipo de Dirección"
							not null,
							// in ventas.CODIFICA(35):descrip,
	prov		char(1)		descr "Provincia"
							mask "1>x"
							not null,
							// in ventas.PROVINCIAS:descrip
	local		num(5)		descr "Localidad de la Provincia"
							// in gral.LOCALPROV(prov):descrip
							not null,
	calle		num(4)		descr "Código de Calle",
							// in gral.CALLE(prov, local):descrip
	nro			num(5)		descr "Número",
	piso		char(2)		descr "Piso"
							mask "2>x",
	depto		char(4)		descr "Departamento"
							mask "4>x",
	ecalle1		num(4)		descr "Entre Primera Calle",
							// in gral.CALLE(prov, local):descrip
	ecalle2		num(4)		descr "Entre Segunda Calle",
							// in gral.CALLE(prov, local):descrip
	cpostal		num(4)		descr "Código Postal",
	apynom		char(50)	descr "Apellido y Nombre / Empresa"
							not null
							mask "50>x",
	flag		num(2)		descr "Flag",
	dlocal		char(30)	descr "Descrip. localidad",
	dcalle		char(35)	descr "Descrip. calle",
	barrio		char(30)	descr "Barrio",
	partido		char(30)	descr "Partido",
	cpa			char(8)		descr "Codigo postal argentino",
	decalle1	char(35)	descr "Descrip. entre calle 1",
	decalle2	char(35)	descr "Descrip. entre calle 2",
	estdirec	num(2)		descr "Estado de validacion con Merlin",
	errdirec	num(2)		descr "Error de la validacion con Merlin"
							default 0,
	activo		bool		descr "Está Activo??"
							not null default true,
}
primary key (codcta, coddir)[24],  //base=3.000.000)
index merlin (estdirec not null)[5];

table CTAATR  				descr "Atributos Adicionales de la Cuenta" {

	codcta		num(8)		descr "Código de Cuenta Asociado"
							not null
							in CTADAT:apynom,
	tipcod		num(2)		descr "Tipo de Código"
	    					not null,
	codigo		char(12)	descr "Código"
	    					not null,
	mdate		date		descr "Fecha de Modificacion",
	mtime		time		descr "Hora de Modificacion",
	muid		num(5)		descr "Usuario que Modifico",
}
primary key (codcta, tipcod)[22],   // base=3.000.000
unique index TIPCOD (tipcod, codcta)[22];

table CTATEL				descr "Números de Teléfono de la Cuenta" {
	codcta		num(8)		descr "Código de Cuenta Asociado"
							not null
							in CTADAT:apynom,
	coddir		num(4)		descr "Código de Dirección"
							in CTADIR(codcta):apynom,
	renglon		num(2)		descr "Renglón"
							not null,
	tiptel		num(2)		descr "Tipo de Teléfono"
							not null,
							// in ventas.CODIFICA(38):descrip,
	ddn			char(5)		descr "DDN o Telediscado"
							not null,
	prefijo		char(4)		descr "Prefijo o Característica",
	sufijo		char(4)		descr "Sufijo o Número de Teléfono"
							not null,
}
primary key (codcta, coddir, renglon)[27];   //base=3.000.000

table CTAMCOM				descr "Medios de Comunicación por Cuenta" {
	codcta		num(8)		descr "Código de Cuenta Asociado"
							not null
							in CTADAT:apynom,
	coddir		num(4)		descr "Código de Dirección"
							in CTADIR(codcta):apynom,
	renglon		num(2)		descr "Renglón"
							not null,
	tipmed		num(2)		descr "Tipo de Medio de Comunicación"
							not null,
							// in ventas.CODIFICA(39):descrip,
	valor 		char(60)	descr "Sufijo o Número de Teléfono"
							not null,
	estado		num(2)		descr "Estado del Registro",
	fecmod		date		descr "Fecha de Modificación",
}
primary key (codcta, coddir, renglon)[27],  //base=3.000.000
index FECHA (fecmod not null) [15],
index ESTADO (estado not null) [15];

table CTACOMPL				descr "Datos complementarios de la dirección" {
	codcta		num(8)		descr "Código de Cuenta Asociado"
							not null
							in CTADAT:apynom,
	coddir		num(4)		descr "Código de Dirección"
							in CTADIR(codcta):apynom,
	descrip		char(40)	descr "Descripción del complemento"
							not null
							mask "40>x",
}
primary key (codcta, coddir)[7];    //base=200.000

table CTACBTE		  	    descr "Comprobantes por Cuenta" {
	tipomov 	num(2)	    descr "Tipo de Movimiento"
						    not null,
						    // in ventas.TIPOMOV
	numcomp		num(8)	    descr "Número de Comprobante"
						    not null,
	codcta		num(8)		descr "Código de Cuenta Asociado"
							not null
							in CTADAT:apynom,
	suc			num(3)		descr "Sucursal del Movimiento"
							not null,
							// in ventas.SUCURSAL:descrip,
	fecha		date		descr "Fecha del Movimiento"
							not null,
	valor		num(11,2)	descr "Monto del Movimiento"
							not null
							default 0.0,
	vendedor	num(4)		descr "Vendedor",
							// in ventas.VENDEDORES : vendnom
	tipoiva		num(2)		descr "Tipo de Iva del Cliente.",
							// in ventas TIVA by VENTAS(true) : descrip
	tipofac		char(1)		descr "Tipo de Factura"
							in ("A":"Fc. Tipo A",
								"B":"Fc. Tipo B",
								"E":"Fc. Tipo E")
							mask ">x",
	seriefac	char(4)		descr "Sucursal Nro."
							mask "4n",
	nrofac		char(8)		descr "Factura Nro."
							mask "8n",
	horacot		time		descr "Hora de la Cotizacion",
	feccot		date		descr "Fecha de la Cotizacion",
}
primary key (tipomov, numcomp)[56],  //maximo separ de ideafix
index CUENTA (codcta, fecha)[56];

table CTACBDIR		  	    descr "Direcciones de los Comprobantes" {
	tipomov 	num(2)	    descr "Tipo de Movimiento"
						    not null,
						    // in ventas.TIPOMOV
	numcomp		num(8)	    descr "Número de Comprobante"
						    not null,
	clase		num(2)		descr "Clase de Dirección : Dir / Suc"
							not null,
							// in ventas.CODIFICA(37):descrip
	coddir		num(4)		descr "Código de Dirección"
							// sin in pues puede ser in CTADIR / in ventas.SUCURSAL
							not null,
	tipdir		num(2) 		descr "Tipo de direccion",
}
primary key (tipomov, numcomp, clase, coddir)[28];  //base=5.000.000

table CTACBATR				descr "Atributos Adicionales del Comprobante" {

	tipomov 	num(2)	    descr "Tipo de Movimiento"
						    not null,
						    // in ventas.TIPOMOV
	numcomp		num(8)	    descr "Número de Comprobante"
						    not null,
	tipcod		num(2)		descr "Tipo de Código"
	    					not null,
	codigo		char(10)	descr "Código"
	    					not null,
}
primary key (tipomov, numcomp, tipcod)[31],  //base=5.000.000
unique index CODIGO (tipcod, tipomov, numcomp) [31];

table CTACBITEM		 	    descr "Items de los Comprobantes" {
	tipomov 	num(2)	    descr "Tipo de Movimiento"
						    not null,
						    // in ventas.TIPOMOV
	numcomp		num(8)	    descr "Número de Comprobante"
						    not null,
	renglon		num(3)		descr "Nro. de Renglón"
							not null,
	codadm 		char(8)		descr "Código del Item del Catálogo"
							not null,
							// in ventas.CATALOGO:des1adm
	cantidad	num(9)		descr "Cantidad"
							not null
							default 0,
	costo		num(11,2)	descr "COSTO UNITARIO"
							not null
							default 0.0,
	adic		num(11,2)	descr "ADICIONALES"
							not null
							default 0.0,
}
primary key (tipomov, numcomp, renglon)[38];  //base=6.000.000

table CTACBMP	 	  	    descr "Medios de Pago de los Comprobantes" {
	tipomov 	num(2)	    descr "Tipo de Movimiento"
						    not null,
						    // in ventas.TIPOMOV
	numcomp		num(8)	    descr "Número de Comprobante"
						    not null,
	renglon		num(3)		descr "Nro. de Renglón"
							not null,
	mpago		num(2)		descr "Medio de Pago"
							not null,
							// in ventas.MEDIOSP
	subm		num(3)		descr "SubMedio de Pago"
							not null,
							// in ventas.SUBMEDIOS(mpago)
	cuotas		num(2)		descr "Cuotas"
							not null,
							// in ventas.CONDMP(mpago, subm)
	capital		num(11,2)	descr "Valor Capital"
							not null
							default 0.0,
	total		num(11,2)	descr "Valor Total"
							not null
							default 0.0,
	mctecap		num(11,2)	descr "Valor Capital en moneda corriente"
							not null
							default 0.0,
	mctetotal	num(11,2)	descr "Valor Total en moneda corriente"
							not null
							default 0.0,

}
primary key (tipomov, numcomp, renglon)[38];  //base=6.000.000

table SOLTARJ			descr "Solicitudes de Tarjeta de GARBARINO" {

    nrosuc      num(3)		descr "Sucursal de la Empresa"
							not null,
	nrosol		num(8)		descr "Número de Solicitud"
							not null,
	dirrecep	num(2)		descr "Domicilio de Recepción del Resumen"
							not null,
							// in CODIFICA(35)
	tipcamp		num(2)		descr "Tipo de Campaña"
							not null,
							// in CODIFICA(25)
	segmento	num(2)		descr "Segmento"
							not null,
							// in CODIFICA(26)
	horariod    time		descr "Horario de Recepción Desde"
							not null,
	horarioh    time		descr "Horario de Recepción Hasta"
							not null,
	pariente	num(2)		descr "Parentesco del Referente"
							not null,
							// in CODIFICA(27)
	vendedor    num(4)		descr "Vendedor"
							not null,
	req_relcuo	bool		descr "Verificado Relación Cuota Ingreso"
							not null default false,
	req_deumax	bool		descr "Verificado Deuda Máxima un Sueldo"
							not null default false,
	req_recsue	bool		descr "Verificado Recibo de Sueldo"
							not null default false,
	req_fotdni	bool		descr "Verificado Fotocopia de DNI"
							not null default false,
	req_verdom	bool		descr "Verificado de Domicilio"
							not null default false,
	req_infmig	bool		descr "Verificado Informe Cámara San Miguel"
							not null default false,
	req_inffid	bool		descr "Verificado Informe FIDELITAS"
							not null default false,
	req_ingmin	bool		descr "Verificado Ingresos Mínimos"
							not null default false,
	req_solcre	bool		descr "Verificado Solicitud de Crédito"
							not null default false,
	req_pagare	bool		descr "Verificado Pagaré"
							not null default false,
	plazo		num(4)		descr "Plazo"
							not null,
	cuota		num(11,2)	descr "Cuota"
							not null,
	val_relcuo	num(6,2)	descr "Valor Verificado Relación Cuota Ingreso"
							not null default 0.0,
	val_deumax	num(11,2)	descr "Valor Verificado Deuda Máxima un Sueldo"
							not null default 0.0,
	numcard		char(20)	descr "Número de Tarjeta Asignada",
	limcred		num(11,2)	descr "Límite de Crédito de la Tarjeta",
	estado		num(2)		descr "Estado de la Solicitud",
	nrocta		num(10)		descr "Número de Cuenta",
    faprob      date        descr "Fecha de Aprobación",
	banco		num(2)		descr "Código de Banco",
    cdate		date		descr "Fecha de Carga",
	ctime		time		descr "Hora de Carga",
	cuid		num(5)		descr "Usuario que Cargo",
	mdate		date		descr "Fecha de Modificacion",
	mtime		time		descr "Hora de Modificacion",
	muid		num(5)		descr "Usuario que Modifico",
}
primary key (nrosuc, nrosol),
unique index NUMCARD (numcard not null),
unique index NROCTA	(nrocta not null),
unique index ALTA (faprob not null, nrosuc, nrosol),
unique index FECHA (nrosuc, cdate, nrosol);

table CLITARJ			descr "Clientes Solicitantes y Codeudores de Tarjeta" {

    nrosuc      num(3)		descr "Sucursal de la Empresa"
							not null,
	nrosol		num(8)		descr "Número de Solicitud"
							not null
							in SOLTARJ(nrosuc),
	tipcli		char(1)		descr "Tipo de Cliente en la Solicitud"
							in ("S":"Solicitante","C":"Codeudor")
							mask ">A"
							default "S",
    codcta		num(8)		descr "Código de Cuenta Corriente del Cliente"
    						not null,
    						// in serv.CTADAT
    nacional    char(20)    descr "Nacionalidad"
							not null,
    estcivil    char        descr "Estado Civil"
    						not null in ("C":"Casado",
    									 "S":"Soltero",
    									 "D":"Divorciado",
    									 "V":"Viudo",
    									 "X":"Separado"),
	sexo		char		descr "Sexo"
							not null in ("F":"Femenino",
										 "M":"Masculino"),
	hijos		num(2)		descr "Cantidad de Hijos",
	estudios    num(2)		descr "Nivel de Estudios"
	                        not null, // in CODIFICA(18)
	tipdocc		char(3) 	descr "Tipo de Documento Conyuge",
	nrodocc  	num(9)		descr "Número de Documento Conyuge",
	nombrec    	char(20)	descr "Nombre Conyuge",
	apellidoc   char(30)	descr "Apellido Conyuge",
	casadep     char        descr "Casa / Departamento"
							not null in ("C":"Casa","D":"Departamento") mask ">A" default "D",
	ambient		num(2)		descr "Cantidad de Ambientes"
							not null,
	tipprop     num(2)		descr "Tipo de Propiedad"
							not null,
							// in CODIFICA(23)
	val_prop	num(11,2)	descr "Valor de la Propiedad"
							not null,
	deudprop    num(11,2)	descr "Deuda de la Propiedad",
 	val_alq		num(11,2)	descr "Valor del Alquiler",
	modelauto	char(20)	descr "Marca y Modelo del Automóvil"
							mask "20>x",
	patente     char(10)	descr "Nro. de Patente"
							mask "10>x",
	val_auto	num(11,2)	descr "Valor del Automóvil",
	deud_auto   num(11,2)	descr "Deuda Automovil Prendado",
	tipemp		char(1)		descr "Tipo de Empleado"
							not null in ("F":"Fijo","C":"Contratado") mask ">A" default "F",
	ocupa		num(2)		descr "Ocupacion"
							not null,
							// in CODIFICA(19)
	cargo		char(20) 	descr "Cargo o Tarea"
							not null mask "20>x",
	area		char(20) 	descr "Area o Sector"
							not null mask "20>x",
	ramo		num(2)		descr "Ramo o Rubro"
							not null,
							// in CODIFICA(20)
	fecing		date		descr "Fecha de Ingreso Estimada en la Empresa"
							not null,
	superior	char(20) 	descr "Nombre del Superior Inmediato"
							mask "20>x",
	ingprop		num(11,2)	descr "Ingresos Propios"
							not null > 0.0,
	conprop		num(2)		descr "Constancia de Ingresos Propios"
							not null,
							// in CODIFICA(21)
	ingadic		num(11,2)	descr "Ingresos Adicionales"
							not null default 0.0,
	conadic		num(2)		descr "Constancia de Ingresos Adicionales",
							// in CODIFICA(22)
	banco		char(20)	descr "Banco"
							mask "20>x",
	bancta      char(15)	descr "Cuenta Bancaria",
	credcard	char(20)	descr "Tarjeta de Crédito"
							mask "20>x",
	nrocard		char(15)	descr "Nro. de Tarjeta de Crédito",
    empcta		num(8)		descr "Código de Cuenta Corriente Laboral (Opcional)",
    						// in serv.CTADAT
}
primary key (nrosuc, nrosol, tipcli),
unique index CUENTA (codcta, nrosuc, nrosol, tipcli);

table BIENTARJ  	descr "Bienes del Solicitante" {
    nrosuc      num(3)		descr "Sucursal de la Empresa"
							not null,
	nrosol		num(8)		descr "Número de Solicitud"
							not null
							in SOLTARJ(nrosuc),
	tipcli		char(1)		descr "Tipo de Cliente en la Solicitud"
							in ("S":"Solicitante","C":"Codeudor","R":"Referencia")
							mask ">A"
							default "S",
	bien		num(2)		descr "Tipo de Bien"
							not null,
							// in CODIFICA(24)
}
primary key (nrosuc, nrosol, tipcli, bien) [7];

table OBSTARJ  	descr "Observaciones de la Solicitud" {
    nrosuc      num(3)		descr "Sucursal de la Empresa"
							not null,
	nrosol		num(8)		descr "Número de Solicitud"
							not null
							in SOLTARJ(nrosuc),
	renglon		num(2)		descr "Número de Renglón"
							not null,
	detalle		char(60)	descr "Detalle"
							not null,
}
primary key (nrosuc, nrosol, renglon);

table COBTARJ	descr "Cobranzas de Tarjeta de Crédito" {
    tipcob		num(2)		descr "Tipo de Cobranza"
    						not null,
    						// in ventas.CODIFICA (32)
	nrocta		num(10)		descr "Número de Cuenta"
							not null,
							//  in SOLTARJ cuando es tarjeta propia
	fpago		date		descr "Fecha de Pago"
							not null,
    nropago     num(2)		descr "Nro. de Pago"
    						not null,
	sucursal	num(3)		descr "Sucursal"
							not null,
	cajero		num(5)		descr "Cajero Asociado"
							not null,
	nrocomp		num(9)		descr "Mov. de Caja Relacionado"
							not null,
	nrores      num(9)		descr "Número de Resumen",
}
primary key (tipcob, nrocta, fpago, nropago) [28],
unique index SUCURSAL (sucursal, fpago, cajero, nrocomp) [80] /* Base 1.500.000 */;

table CTATARJ	descr "Cuentas de la Tarjeta de Crédito Propia" {
	nrocta		num(10)		descr "Número de Cuenta",
	nombre    	char(20)	descr "Nombre"
							not null,
	apellido    char(30)	descr "Apellido"
							not null,
	numcard		char(20)	descr "Número de Tarjeta Asignada"
							not null,
    nrosuc      num(3)		descr "Sucursal de la Empresa",
	nrosol		num(8)		descr "Número de Solicitud"
							in SOLTARJ(nrosuc),
	fcieact		date		descr "Ult. Fecha de Cierre",
	fvencim		date		descr "Ult. Fecha de Vencimiento",
    limcom      num(11,2)   descr "Ult. Límite de Compra",
    pagmin      num(11,2)   descr "Ult. Pago Mínimo",
    salpes      num(11,2)   descr "Ult. Saldo en Pesos",
    saldol      num(11,2)   descr "Ult. Saldo en Dólares",
	tipdoc		char(3)		descr "Tipo de Documento"
							mask "3>x",
	nrodoc		num(8)		descr "Número de Documento",
    cdate		date		descr "Fecha de Carga",
	ctime		time		descr "Hora de Carga",
	cuid		num(5)		descr "Usuario que Cargo",
	mdate		date		descr "Fecha de Modificacion",
	mtime		time		descr "Hora de Modificacion",
	muid		num(5)		descr "Usuario que Modifico",
}
primary key (nrocta),
unique index numcard (numcard),
unique index nrosuc	(nrosuc not null, nrosol not null),
index tipdoc (tipdoc not null, nrodoc not null);

table LIMVAL				descr "Límites de Valores" {

	tipo		num(2)		descr "Tipo de Código"
							not null,
							// in CODIFICA () : descrip
	base		num(11,2)	descr "Base"
							not null,
	valor		num(11,2)	descr "Valor Asociado"
							not null,
}
primary key (tipo, base);

table COMPTARJ			descr "Datos Complementarios de Solicitudes de Tarjeta" {

    nrosuc      num(3)		descr "Sucursal de la Empresa"
							not null,
	nrosol		num(8)		descr "Número de Solicitud"
							not null,
    fpago		date		descr "Fecha de Pago"
    						not null,
	cajero		num(5)		descr "Cajero Asociado"
							not null,
	nrocomp		num(9)		descr "Mov. de Caja Relacionado"
							not null,
    valgas		num(11,2)	descr "Valor de Gastos Administrativos"
    						not null default 0.0,
    valsel		num(11,2)	descr "Valor del Sellado"
    						not null default 0.0,
    ambient     num(2)	    descr "Empresa Ambiental",
    serimp      char(4)     descr "Serie Preimpresa",
    nroimp      char(8)     descr "Número Preimpreso",
    cdate		date		descr "Fecha de Carga",
	ctime		time		descr "Hora de Carga",
	cuid		num(5)		descr "Usuario que Cargo",
}
primary key (nrosuc, nrosol),
unique index SUCURSAL (nrosuc, fpago, cajero, nrocomp);

table CBTEGAR		 	    descr "Comprobantes para Garantía" {
	tipomov 	num(2)	    descr "Tipo de Movimiento"
						    not null,
						    // in ventas.TIPOMOV
	numcomp		num(8)	    descr "Número de Comprobante"
						    not null,
	fecha		date		descr "Fecha de Facturación"
							default today,
	sucursal    num(3)      descr "Sucursal donde se realizo la venta",
	tipofac		char(1)		descr "Tipo de Factura"
							in ("A":"Fc. Tipo A",
								"B":"Fc. Tipo B",
								"E":"Fc. Tipo E")
							mask ">x",
	seriefac	char(4)		descr "Sucursal Nro."
							mask "4n",
	nrofac		char(8)		descr "Factura Nro."
							mask "8n",
	nombre	     char(30)   descr "Nombre del Cliente."
							not null,
	direccion	 char(30)	descr "Dirección del Cliente."
							not null,
	local		 char(30)	descr "Localidad del Cliente.",
	prov		char(1)		descr "Provincia"
							// in ventas.PROVINCIAS : descrip
							not null
							mask ">x",
	cpostal		 num(4)		descr "Codigo postal",
	tipdoc		 char(3)	descr "Tipo de Documento del Cliente.",
	nrodoc		 num (9)	descr "Nro. de Documento del Cliente.",
	area		char(5)		descr "Numero de DDN",
	telefono	char(8)		descr "Numero de telefono",
	codcta		num(8)		descr "Código de Cuenta",
	nrolista	num(6)		descr "Número de Lista de Casamiento",
}
primary key (tipomov, numcomp)[37],       // base 8 millones
index NOMBRE (nombre)[56],
index DOCUM (tipdoc, nrodoc)[45],
unique index FACTURA (tipofac not null, seriefac not null, nrofac not null, tipomov, numcomp)[56];

table CBTEMGAR		 	    descr "Comprobantes para Garantía (mov)" {

	tipomov 	num(2)	    descr "Tipo de Movimiento"
						    not null,
						    // in ventas.TIPOMOV
	numcomp		num(8)	    descr "Número de Comprobante"
						    not null,
	renglon		num(3)		descr "Nro. de  Renglón"
							not null,
	codadm 		char(8)		descr "Código del Item del Catálogo"
							not null,
							// in ventas.CATALOGO:des1adm
	cantidad	num(9)		descr "Cantidad factible de asociar gar.ext."
							not null
							default 0,
	costo		num(11,2)	descr "Costo"
							not null
							default 0.0,
	cantvend	num(9)		descr "Cantidad vendida del artículo"
							not null
							default 0,
	cantncred	num(9)		descr "Cantidad de notas de cred. de gar. ext"
							not null
							default 0,
	fcadgf		date		descr "Fecha de Caducidad Garantia Fabricante",
	diasge		num(3)		descr "Cantidad de Dias p/venta G.Ext."
							default 0
							not null
}
primary key (tipomov, numcomp, renglon)[53],  //base=12.000.000
index FECCAD (fcadgf)[29];

table CBTEOBS				descr "Relación Cbte - Observ. Generales" {

	tipomov 	num(2)	    descr "Tipo de Movimiento"
						    not null,
						    // in ventas.TIPOMOV
	numcomp		num(8)	    descr "Número de Comprobante"
						    not null,
	renglon		num(3)		descr "Nro. de  Renglón",

	tipobs		num(4)		descr "Tipo de Observación"
							not null, //in ventas.sernum
	codobs		num(8)		descr "Numero de Observacion"
							not null,
}
primary key(tipomov, numcomp, renglon, tipobs) [11];

table PERGCUO				descr "Porcentajes de Comisiones por Períodos de Garantía y Cuotas" {

	codper		num(4)		descr "Código de periodo"
							in PERGAR:descor
							not null,
	cuotas		num(2)		descr "Cantidad de cuotas - pagos"
							// in CONDMP (codigo, subcod) : (tasa, activo)
							not null,
	fechad		date		descr "Fecha de vigencia (desde)"
							not null,
	fechah		date		descr "Fecha de vigencia (Hasta)",
	porc		num(4,2)	descr "Porcentaje de Comisión"
							not null,
}
primary key (codper, cuotas, fechad);

table COMLINEA				descr "Porcentajes de Comisiones por Línea" {

	tipo		num(2)		descr "Tipo de Código"
							not null,
							// in CODIFICA () : descrip
	agrup		num(2)		descr "Agrupamiento de Familia"
							//in ventas.CODIFICA (4) : descrip
							not null,
	fecha		date		descr "Fecha de vigencia"
							not null,
	escala		num(5,2)	descr "Escala (margen)"
							not null,
	valor		num(5,2)	descr "Porcentaje de Comisión"
							not null,
}
primary key (tipo, agrup, fecha, escala);

table VALCATEG				descr "Valores por Categoría para Tarjeta" {

	categ		num(2)		descr "Categoría para Tarjeta"
							// in CODIFICA(58)
							not null,
	fdesde		date		descr "Fecha desde"
							not null,
    limite      num(5,2)    descr "Límite"
                            not null,
	valor		num(10,2)	descr "Valor"
							not null,

}
primary key (categ, fdesde);


table CTAREL				descr "Relaciones entre clientes del CTADAT" {

	codctaorig	num(8)		descr "Código de Cuenta Original"
							not null
							in CTADAT:apynom,
	tiprel		num(2)		descr "Tipo de Relación"
							// in CODIFICA(59)
							not null,
	codctadest	num(8)		descr "Código de Cuenta Destino/Relacionada"
							not null
							in CTADAT:apynom,
	vigencia	date		descr "Vigencia de la relación",
    cdate		date		descr "Fecha de Alta",
	ctime		time		descr "Hora de Alta",
	cuid		num(5)		descr "Usuario que dio el alta",
	mdate		date		descr "Fecha de Modificacion",
	mtime		time		descr "Hora de Modificacion",
	muid		num(5)		descr "Usuario que Modifico",

}
primary key(codctaorig, tiprel, codctadest),
unique index TIPREL (tiprel, codctadest, codctaorig);

table REGDTV  descr 		"Registración Direct Tv"
{
	tipomov		num(2)	descr	"Tipo de Comprobante"
						not null,
	numcomp		num(8)	descr	"Número de Comprobante"
						not null,
	fefact		date	descr	"Fecha de Facturacion"
						not null,
	numord		char(10) descr	"Número de Orden de Emisión",
						//mask "2N-7N",
	fecvto		date	descr	"Fecha de Activacion",
	fecinst		date	descr	"Fecha de Instalación",
	fecconf		date	descr	"Fecha de Envío",
	sucursal	num(3)	descr	"Sucursal"
						not null,
	estado		num(2)	descr	"Estado"
						not null,
	codcta		num(8)	descr	"Código de Instalador",
	codadm      char(8)	descr	"Código de Artículo",
	renglon		num(2)	descr	"Renglón",
						// No es el del MOVIM
						// es por si movim_cantf > 1
	reng		num(2)	descr	"Renglón del MOVIM",
	suscrip		char(8)	descr	"Suscripcion Adquirida ",
						// in ventas.CATALOGO
	vendedor	num(4)	descr	"Vendedor",
						//in VENDEDORES : vendnom,
	vendserv	num(4)	descr	"Vendedor Servicio",
						//in VENDEDORES : vendnom,
	entiempo	bool 	descr	"En Tiempo",
	enforma		bool 	descr	"En Forma",
	fcomis		date	descr	"Fecha de Comision",
	fbaja		date	descr	"Fecha Baja",
	frech		date  	descr	"Fecha de Rechazo",
	impcomis	num(10,2) descr	"Importe Comision",
	impbaja		num(10,2) descr	"Importe Baja",
	tipsuscr	num(4)	descr	"Tipo de Suscripcion"
								, //in GRUPDCAT (20)
	rechazo		bool	descr	"Fue Rechazado ?",
	nroext		num(10)	descr	"Nro. Externo",
	mpago		num(2)	descr	"Medio de Pago Fibertel",
	flag		num(4)	descr	"Flag",
	fldaux		char(20) descr	"Campo Auxiliar",
	wftipo		num(2)	descr   "Tipo de Workflow",
	serie		char(15) descr	"Nro. Serie-IMEI",
	movil		char(10) descr	"Nro. Móvil",
	simcard		char(20) descr	"Nro. Simcard",
	fregis		date	descr	"Fecha de Registracion",
	estact		num(1)	descr	"Estado de la Activacion",
	cdate		date	descr	"Fecha de Alta",
	ctime		time	descr	"Hora de Alta",
	cuid		num(5)	descr	"Usuario que dio el alta",
	mdate		date	descr	"Fecha de Modificacion",
	mtime		time	descr	"Hora de Modificacion",
	muid		num(5)	descr	"Usuario que Modifico",

}primary key	(tipomov, numcomp, renglon)[4], //base 100.000
index FECINS	(fecinst)[3],
index ORDEN		(numord not null)[6],
index FCOMIS	(fcomis not null, vendserv)[3],
index FBAJA		(fbaja not null, vendserv)[3],
index FECCONF	(fecconf not null)[3],
index FRECH		(frech not null)[3],
unique index SUCFECH	(sucursal, fefact, tipomov, numcomp, renglon)[6],
unique index IMEI (serie not null) [ 6 ],
index NROMOVIL    (movil not null) [ 6 ],
index SIM         (simcard not null) [ 6 ],
index NUMORD      (wftipo,tipsuscr,numord)[6],
index FREGIS	  (fregis not null, vendserv) [3];

table REGCEL descr "Registración de celulares"
{
	nvta		num(8)		descr	"Número de nota de venta"
							not null,
	renglon		num(2)		descr	"Renglón"
							// No es el del MOVIM
							// es por si movim_cantf > 1
							not null,
	codadm      char(8)		descr	"Código de Artículo"
							not null,
	fefact		date		descr	"Fecha de Facturación"
							not null,
	sucursal    num(3)		descr	"Sucursal donde se realizo la venta"
							//in SUCURSAL : descor
							not null,
	vendedor	num(4)		descr	"Vendedor"
							not null,
							//in VENDEDORES : vendnom,
	estado		num(2)		descr	"Condición de aprobación de la N.V."
							not null,
	reng		num(2)		descr	"Renglón del MOVIM"
							not null,
	serie		char(15)	descr	"Nro. Serie-IMEI",
	movil		char(10)	descr	"Nro. Móvil",
	nrosol		num(12)		descr	"Nro. Solicitud",
	numrel		num(8)		descr	"Nvi. Relacionado",
	fbaja		date		descr	"Fecha Baja",
	numserv		num(8)		descr	"Nvi. Servicio",
	vendserv	num(4)		descr	"Vendedor Servicio",
	debiaut		bool		descr	"Por Débito Automático",
	movemer		bool		descr	"Movicom de Emergencia",
	recibo		char(14)	descr	"Nro. de Recibo",
	marca		num(4)		descr	"Marca del Movil"
									, //in GRUPDCAT (98)
	abono		char(8)		descr	"Abono Adquirido"
									, // in ventas.CATALOGO
	entiempo	bool		descr	"En Tiempo",
	enforma		bool		descr	"En Forma",
	fcomis		date		descr	"Fecha de Comision",
	wfestado	num(3)		descr	"Estado del Workflow",
	impcomis	num(10,2)	descr	"Importe Comision",
	impbaja		num(10,2)	descr	"Importe Baja",
	codaut      char(8)     descr   "Codigo Autorizacion Tarjeta",
	mtarjeta    char(20)    descr   "Marca Tarjeta",
	nrotarj     char(20)    descr   "Nro. Tarjeta",
	ventarj     date		descr   "Vencimiento Tarjeta",
	impresa     bool        descr   "Se imprimio?"
									default false,
	penaliz     bool        descr   "Fue penalizado?",
	fingr		date		descr	"Fecha de Ingreso",
	frech		date		descr	"Fecha de Rechazo",
	fbajacom	date		descr	"Fecha de Baja Compania",
	simcard		char(20)	descr	"Nro. Simcard",
	wftipo		num(2)		descr "Tipo de Workflow",
							//in wftflow(wfemp):(wfdescr),
	vendreg		num(4)		descr	"Vendedor que registro el IMEI",
	fecact      date        descr   "Fecha Activacion",
	estact		num(1)		descr	"Estado de la Activacion",
	nmusim		char(30)	descr	"Nro. NMU de tarjeta SIM",
	nmuimei		char(30)	descr	"Nro. NMU de equipo",
	respsim		num(1)		descr	"Codigo respuesta tarjeta SIM",
	respimei	num(1)		descr	"Codigo respuesta equipo",
	seq_id		num(10)		descr	"Nro. secuencia en ORACLE (REGISTRO_CELULAR)",
	codresp		num(2)		descr	"Codigo de respuesta de ACTICEL",
									//in CODIFICA(364)
	cdate		date		descr	"Fecha de Alta",
	ctime		time		descr	"Hora de Alta",
	cuid		num(5)		descr	"Usuario que dio el alta",
	mdate		date		descr	"Fecha de Modificacion",
	mtime		time		descr	"Hora de Modificacion",
	muid		num(5)		descr	"Usuario que Modifico",
}
primary key (nvta, renglon) [ 6 ], // Base 200.000
index NROSOL (marca, nrosol not null) [ 9 ],
unique index SERIE (marca, serie not null) [ 10 ],
unique index SUCFECH (sucursal, fefact, nvta, renglon) [ 7 ],
index MOVIL     (movil not null) [ 9 ],
index FCOMIS	(fcomis not null, vendserv) [ 5 ],
index FBAJA		(fbaja not null, vendserv) [ 5 ],
index FINGR		(fingr not null) [ 5 ],
index FRECH		(frech not null) [ 5 ],
index FBAJACOM	(fbajacom not null) [ 5 ],
index ESTADO    (estado, sucursal, fefact) [ 5 ],
index SIMCARD    (simcard not null) [ 5 ];

table PEDDEP  				descr "Pedidos a Depósito"  {
	nroped  	num(8)		descr "Número de Pedido"
							not null,
	fecha   	date  		descr "Fecha del Pedido"
							not null
							default today,
	coddep  	num(3)		descr "Código de Deposito que envía"
							not null,
	codsuc  	num(3)		descr "Código de Sucursal que recibe"
							not null,
	estado		num(2)		descr "Estado del Pedido"
							not null,
	tipisla		num(2)		descr "Tipo de Isla",
    cdate		date		descr "Fecha de Carga",
	ctime		time		descr "Hora de Carga",
	cuid		num(5)		descr "Usuario que Cargo",
	mdate		date		descr "Fecha de Modificacion",
	mtime		time		descr "Hora de Modificacion",
	muid		num(5)		descr "Usuario que Modifico",
}
primary key (nroped)[3],       // Base 100.000
unique index DEPOS (coddep, nroped)[4],
unique index ESTADO (estado, coddep, codsuc, nroped)[6],
unique index FECHA (fecha, nroped)[4],
unique index SUC (estado, codsuc, nroped)[5];

table SERFAC		descr "Nros de Series de Facturación" {

	sucursal	num(3)		descr "Número de Sucursal"
							not null, //in ventas.sucursal.
	tipo		char(1)		descr "Tipo de Operacion"
							in ("A": "Automático",
								"M": "Manual",
								"V": "Viejo",
								"E": "Electrónico")
							mask ">x"
							not null,
	tipofac		char(1)		descr "Tipo de Factura"
							in ("A":"Fc.  Tipo A",
								"B":"Fc.  Tipo B",
								"Y":"Rec. Tipo A",
								"Z":"Rec. Tipo B",
								"X":"Rec. Tipo X",
								"S":"Rec. Senia X",
								"R":"Remito",
								"E":"Exportacion")
							not null
							mask ">x",
	fecha		date		descr "Fecha de Vigencia"
							not null,
	serie		num(4)		descr "Nro de Serie"
							not null,
	nroact		num(8)		descr "Nro actual de la Serie"
							not null,
	minimo		num(8)		descr "Mínimo de la Serie"
							not null,
	maximo		num(8)		descr "Maximo de la Serie"
							not null,
	nroalarm	num(8)		descr "Alarma",
	activo		bool		descr "Activo"
							default false
							not null,
	fecalta		date		descr "Fecha de alta en DGI"
							not null,
	fecbaja		date		descr "Fecha de baja en DGI",
	letra		char(1)		descr "Letra a Imprimir"
							in ("A":"Tipo A",
								"B":"Tipo B",
								"E":"Tipo E",
								"X":"Tipo X"),
	cdate		date		descr "Fecha de Carga",
	ctime		time		descr "Hora de Carga",
	cuid		num(5)		descr "Usuario que Cargo",
	mdate		date		descr "Fecha de Modificacion",
	mtime		time		descr "Hora de Modificacion",
	muid		num(5)		descr "Usuario que Modifico",
}
primary key (tipofac, serie),
index SUCURSAL (sucursal, tipo, tipofac, activo),
index SERIE (serie);

table CAI	descr "Nros de CAI" {

	tipofac		char(1)		descr "Tipo de Factura"
							in ("A":"Fc. Tipo A",
								"B":"Fc.  Tipo B",
								"Y":"Rec. Tipo A",
								"Z":"Rec. Tipo B",
								"R":"Remito",
								"E":"Exportacion",
								"X":"Recibo X",
								"S":"Recibo Senia")
							not null
							mask ">x",
	serie		num(4)		descr "Nro de Serie"
							not null,
	fdesde		date		descr "Fecha desde"
							not null,
	nrocai		char(20)	descr "Nro de CAI"
							not null,
	fhasta		date		descr "Fecha Hasta"
							not null,
}
primary key(tipofac, serie, fdesde);

table SERMAN descr "Carga Series Manuales"
{
	fecha		date		descr "Fecha"
							not null,
	sucursal	num(3)		descr "Sucursal"
							not null,
	tipoA		char(1)		descr "Tipo de Factura A",
	serieA		num(4)		descr "Nro de Serie",
	inicA		num(8)		descr "Nro. Inicial de la Serie",
	finA		num(8)		descr "Nro. Final de la Serie",
	cantA		num(4)		descr "Cantidad de Manuales Tipo A",
	inicokA		num(8)		descr "Nro. Inicial Correcto de la Serie",
	finokA		num(8)		descr "Nro. Final Correcto de la Serie",
	cantokA		num(4)		descr "Cantidad de Manuales Tipo A",
	tipoB		char(1)		descr "Tipo de Factura B",
	serieB		num(4)		descr "Nro de Serie",
	inicB		num(8)		descr "Nro. Inicial de la Serie",
	finB		num(8)		descr "Nro. Final de la Serie",
	cantB		num(4)		descr "Cantidad de Manuales Tipo B",
	inicokB		num(8)		descr "Nro. Inicial Correcto de la Serie",
	finokB		num(8)		descr "Nro. Final Correcto de la Serie",
	cantokB		num(4)		descr "Cantidad de Manuales Tipo B",
	estado		num(2)		descr "Estado"
							/*in ventas.codifica(64) */
							not null,
	geren		num(7)		descr "Código de Gerente",
	subge		num(7)		descr "Código de Subgerente",
	codctrl		num(7)		descr "Código de Controlador",
	cdate		date		descr "Fecha de Carga",
	ctime		time		descr "Hora de Carga",
	cuid		num(5)		descr "Usuario que Cargo",
	mdate		date		descr "Fecha de Modificacion",
	mtime		time		descr "Hora de Modificacion",
	muid		num(5)		descr "Usuario que Modifico",

}primary key(fecha,sucursal);

table SERMANU		descr "Series Manuales anuladas"
{

	fecha		date		descr "Fecha"
							not null,
	sucursal	num(3)		descr "Sucursal"
							not null, /*in ventas.sucursal.*/
	renglon		num(2)		descr "Renglon"
							not null,
	tipofac		char(1)		descr "Tipo de Factura"
							in ("A":"Fc. Tipo A",
								"B":"Fc. Tipo B",
								"E":"Fc. Tipo E",
								"Y":"Rec. Tipo A",
								"Z":"Rec. Tipo B",
								"X":"Rec. Tipo X",
								"S":"Rec. Senia X")
							not null
							mask ">x",
	serie		num(4)		descr "Nro de Serie"
							not null,
	numero		num(8)		descr "Nro Comprobante anulado"
							not null,
	tipomov		num(2)		descr "Tipo de Movimiento",
	cbteok		bool		descr "Comprobante OK"
				default false,
}primary key(fecha, sucursal, renglon);


table CTACBDEV	 	    descr "Items de los Comprobantes devueltos" {

	tipomov 	num(2)	    descr "Tipo de Movimiento"
						    not null,
						    // in ventas.TIPOMOV
	numcomp		num(8)	    descr "Número de Comprobante"
						    not null,
	renglon		num(3)		descr "Nro. de Renglón"
							not null,
	cantd		num(9)		descr "Cantidad Devuelta"
							not null
							default 0,
}
primary key (tipomov, numcomp, renglon)[38];  //base=6.000.000

table RELIMPR			descr "Relaciones entre Comprobantes Impresos" {

	emp			num(2)		descr "Empresa"
							not null,
	tipcomp		num(4)		descr "Tipo de Comprobante Asiento Relacionado"
							not null,
	serie		num(4)		descr "Serie de Comprobante Asiento Relacionado"
							not null,
	nrocomp		num(9)		descr "Nro. de Comprobante Asiento Relacionado"
							not null,
	tipofac		char(1)		descr "Tipo de Comprobante Impreso"
							not null,
	seriefac	num(4)		descr "Nro de Serie Impreso"
							not null,
	nrofac		num(8)		descr "Nro de Comprobante Impreso"
							not null,
	fecha		date		descr "Fecha",
	estado		char(1)		descr "Estado del Comprobante"
     						in ("F":"Facturada",
								"I":"Pendiente de Impresion",
								"Z":"CF - Error")
							default "Z"
							mask ">A",
}
primary key (emp, tipcomp, serie, nrocomp),
index IMPRE (tipofac, seriefac, nrofac);

table TCMOVSER				descr "Series Autoimprimibles" {
	emp			num(2)		descr "Empresa"
							not null,
	tipcomp		num(4)		descr "Tipo de cbte de AURUS"
								not null,
	serie  		num(4)		descr "Serie de AURUS"
								not null,
	autoimp		bool  		descr "Es AutoImprimible ?"
							not null
	                        default false,
	tipofac		char(1)		descr "Tipo de factura",
	cbtecf		num(3)		descr "Tipo Cbte C.Fiscal.",
}
primary key (emp, tipcomp, serie);

table VALLIN				descr "Valores por Línea" {

	tipo		num(3)		descr "Tipo de Valor"
							// in TIPVAL : descrip
							not null,
	suc			num(3)		descr "Sucursal"
							// in SUCURSAL : descor
							not null,
	linea		num(2)		descr "Código de Línea"
							// in CODIFICA
							not null,
	fdesde		date		descr "Fecha desde"
							not null,
	fhasta		date		descr "Fecha hasta",
	valor		num(10,2)	descr "Valor"
							not null,

}
primary key (tipo, suc, linea, fdesde);

table ULTCBTEM		descr "Últimos comprobantes de mes" {

	anio		num(4)		descr "Año de procesamiento"
							not null,
	mes			num(2)		descr "Mes de procesamiento"
							not null,
	tipofac		char(1)		descr "Tipo de Factura"
							in ("A":"Fc. Tipo A",
								"B":"Fc.  Tipo B",
								"Y":"Rec. Tipo A",
								"Z":"Rec. Tipo B",
								"X":"Rec. Tipo X",
								"S":"Rec. Senia X",
								"R":"Remito",
								"E":"Exportacion")
							not null
							mask ">x",
	serie		num(4)		descr "Nro de Serie"
							not null,
	ultnum		num(8)		descr "Ult numero registrado"
							not null,
}
primary key(anio, mes, tipofac, serie);

table PROVCATF				descr "Proveedores de Art. (x fecha)" {

	codadm		char(8)		descr "Código de Administración"
							not null,
							// in VENTAS.CATALOGO
	emp			num(2)		descr "Empresa"
							not null,
							// in AURUS.EMPS
	tipccte		num(2)		descr "Tipo de Cuenta Corriente del Proveedor"
							not null,
							// in AURUS.TCCTES
	nroccte		num(6)		descr "Número de Cuenta Corriente del Proveedor"
							not null,
							// in AURUS.CCTES
	fecha		date		descr "Fecha"
							not null
							default today,
	activo		bool		descr "Está Activa la Relación p/Calculo de Comisión"
							not null default true,
}
primary key (emp, tipccte, nroccte, codadm, fecha),
unique index CODADM (codadm, emp, tipccte, nroccte, fecha);

table PROVADICF				descr "Datos Adicionales de Prov. (x fecha)" {

	emp			num(2)		descr "Empresa"
							not null,
							// in AURUS.EMPS
	tipccte		num(2)		descr "Tipo de Cuenta Corriente del Proveedor"
							not null,
							// in AURUS.TCCTES
	nroccte		num(6)		descr "Número de Cuenta Corriente del Proveedor"
							not null,
							// in AURUS.CCTES
	fecha		date		descr "Fecha"
							not null
							default today,
	activo		bool		descr "Está Activa la Relación para FRU"
							not null default true,
}
primary key (emp, tipccte, nroccte, fecha);

table CTAOBS		descr "Requisitos y Condiciones para Clientes Corporativos" {

	codcta		num(8)		descr "Código de Cuenta"
							not null > 0
							in CTADAT:apynom,
	fdesde		date		descr "Fecha Desde"
							not null,
	fhasta		date		descr "Fecha Hasta",

	codreq		num(8)		descr "Código de Requisito",

	codcond		num(8)		descr "Código de Condición",
}
primary key (codcta, fdesde);

table REGCANAL  descr 		"Registración Multicanal"
{
	tipomov		num(2)		descr	"Tipo de Comprobante"
				not null,
	numcomp		num(8)		descr	"Número de Comprobante"
				not null,
	renglon		num(2)		descr	"Renglón"
							// No es el del MOVIM
							// es por si movim_cantf > 1
							not null,
	fefact		date		descr	"Fecha de Emisión"
				not null,
	codadm      char(8)		descr	"Código de Artículo"
							not null,
	vendedor	num(4)		descr	"Vendedor"
							not null,
							//in VENDEDORES : vendnom,
	codcta		num(8)		descr	"Código de Cliente"
							not null
							in CTADAT:(apynom),
	numsol		char(12)	descr	"Número de Solicitud",
	fecinst		date		descr   "Fecha de Instalación",
	hordes		time		descr	"Hora Desde",
	horhas		time		descr	"Hora Hasta",
	tarjeta		char(20) 	descr	"Tarjeta",
	nrotar		char(17) 	descr   "Nro. de Tarjeta",
	fecvto		char(4)		descr	"Fecha de Vencimiento",
	sucursal	num(3)		descr	"Sucursal"
				not null,
	estado		num(2)		descr	"Estado"
				not null,
	plan		char(1)		descr	"Plan",
	climult		char(1)		descr	"Cliente Multicanal (S/N)",
	numrel		num(8)		descr	"Número de Comp. Relacionado",
	estmul		num(2)		descr	"Estado Multicanal",
	fecreg		date		descr	"Fecha de Registración",
	coddir		num(4)		descr	"Código de Dirección",
	cdate		date		descr	"Fecha de Alta",
	ctime		time		descr	"Hora de Alta",
	cuid		num(5)		descr	"Usuario que dio el alta",
	mdate		date		descr	"Fecha de Modificacion",
	mtime		time		descr	"Hora de Modificacion",
	muid		num(5)		descr	"Usuario que Modifico",

}primary key(tipomov, numcomp, renglon),
unique index FECHA (tipomov, fefact, numcomp, renglon);


table LOTEFAC		descr "Lotes de Facturación"
{
	nrolote		num(8)		descr "Número de Lote"
							not null,
	tipimpr		num(1)		descr "Tipo de Impresión"
							not null,
							//in ventas.CODIFICA(71)
	cantmov		num(4)		descr "Cantidad de comprobantes"
							not null
							default 0,
	cantimpr	num(4)		descr "Cantidad de impresiones"
							not null
							default 0,
	suc			num(3)		descr	"Sucursal"
							not null,
	cdate		date		descr	"Fecha de Alta",
	ctime		time		descr	"Hora de Alta",
	cuid		num(5)		descr	"Usuario que dio el alta",
	mdate		date		descr	"Fecha de Modificacion",
	mtime		time		descr	"Hora de Modificacion",
	muid		num(5)		descr	"Usuario que Modifico",
}
primary key (nrolote),
unique index SUC (suc, nrolote);

table LOTEFACM		descr "Detalle de Lotes de Facturación"
{
	nrolote		num(8)		descr "Número de Lote"
							in LOTEFAC
							not null,
	renglon		num(4)		descr "Renglones"
							not null,
	tipomov		num(2)		descr	"Tipo de Comprobante"
							not null,
	numcomp		num(8)		descr	"Número de Comprobante"
							not null,
	cantimpr	num(4)		descr "Cantidad de impresiones"
							not null,
}
primary key (nrolote, renglon) [8],
index CBTE (tipomov, numcomp);

table INVALCLI				descr "Datos de Clientes Inválidos" {

	codcta		num(8)		descr "Código de Cuenta"
							not null > 0,
	apynom		char(50)	descr "Apellido y Nombre / Empresa"
							mask "50>x"
							not null,
	tipdoc		char(3)		descr "Tipo de Documento"
							mask "3>x",
							// in gral.TIPDOC : descrip,
	nrodoc		num(8)		descr "Número de Documento",
	suc			num(3)		descr "Sucursal de Alta"
	   						not null,
							// in ventas.SUCURSAL:descrip
	codobs		num(8)		descr "Numero de Observacion",
    cdate		date		descr "Fecha de Carga",
	ctime		time		descr "Hora de Carga",
	cuid		num(5)		descr "Usuario que Cargo",
	mdate		date		descr "Fecha de Modificacion",
	mtime		time		descr "Hora de Modificacion",
	muid		num(5)		descr "Usuario que Modifico",
	dudoso		bool		descr "Es dudoso ?"
							not null
							default true,
}
primary key (codcta)[4],            // Base : 100.000
index APELLIDO (apynom(0,25))[14],
index DOCUM (tipdoc not null, nrodoc not null)[6];


table RPOLIZAS		descr "Relacion de Polizas" {

	tipomov		num(2)		descr	"Tipo de Comprobante"
							not null,
	numcomp		num(8)		descr "Número de Comprobante"
							not null,
	renglon		num(3)		descr "Renglon de Comprobante"
							not null,
	poliza		num(6)		descr "Póliza Aplicada"
							not null,
	numgar		num(8)		descr "Número de Póliza Aplicada"
							not null
							in GAREXT(poliza): estado,
	rpoliza		num(6)		descr "Póliza Renovac",
	rnumgar		num(8)		descr "Número de Póliza Renovac"
							in GAREXT(rpoliza): estado,
}
primary key(tipomov, numcomp, renglon),
index APLIC (poliza, numgar);

table RPERGAR		descr "Relaciones de Periodos de garantias" {

	codper		num(4)		descr "Código de periodo"
							in PERGAR:descrip
							not null,
	codperap	num(4)		descr "Código de periodo que aplica"
							in PERGAR:descrip
							not null,

}
primary key (codper, codperap);

table HOCPRA		descr "Cabecera de Ordenes de Compra"
{
	numcomp		num(8)		descr "Número de Comprobante"
							not null,
	fecgen		date		descr "Fecha de Generación"
							not null,
	codcta		num(8)		descr "Código de Cuenta"
							not null > 0,
	vendedor	num(4)		descr "Vendedor"
							not null,
							// in ventas.VENDEDORES : vendnom
	estado		num(2)		descr "Estado"
							not null,
	sucursal	num(3)		descr "Sucursal"
							not null,
							// in ventas.SUCURSAL : descor
	redondeo	num(5,2)	descr "Redondeo",
	incdesc     num(1)      descr "Importe de Ordenes de Compra"
							in(	0: "Sin Descuento Especial",
								1: "Con Descuento Especial",
								2: "Precio Manual")
							not null
							default 0,
	tipord		num(1)		descr "Tipo de Orden"
							not null
							in (0:"Standard",1:"Por Beneficiario")
							default 0,
	cdate		date		descr "Fecha de Carga",
	ctime		time		descr "Hora de Carga",
	cuid		num(5)		descr "Usuario que Cargo",
	mdate		date		descr "Fecha de Modificacion",
	mtime		time		descr "Hora de Modificacion",
	muid		num(5)		descr "Usuario que Modifico",

}primary key(numcomp)[5],    // Base 200.000
index CLIENTE(codcta)[5],
index PEND(estado)[3],
index SUCFECH(sucursal, fecgen, tipord, numcomp)[9];	//Base 250.000

table DOCPRA		descr "Detalle de Ordenes de Compra"
{
	numcomp		num(8)		descr "Número de Comprobante"
							in HOCPRA:codcta
							not null,
	renglon		num(3)		descr "Renglón"
							not null,
	cant		num(9)		descr "Cantidad"
							 not null default 0,
	valor		num(11,2)	descr "Monto"
							not null default 0.0,
	codadm		char(8)		descr "Artículo"
							not null,
	cdate		date		descr "Fecha de Carga",
	ctime		time		descr "Hora de Carga",
	cuid		num(5)		descr "Usuario que Cargo",
	mdate		date		descr "Fecha de Modificacion",
	mtime		time		descr "Hora de Modificacion",
	muid		num(5)		descr "Usuario que Modifico",

}primary key(numcomp,renglon)[6];  // Base 200.000

table AOCPRA			descr "Aplicación de Ordenes de Compra"
{
	numcomp		num(8)		descr "Número de Comprobante"
							not null,
	numord		num(8)		descr "Nro de Orden de Compra"
							in OCPRA(numcomp):valor
							not null,
	aplic		num(2)		descr "Nro. de Aplicación"
							not null,
	tipaplic	num(2)		descr "Tipo de Comprobante "
							not null,
	nroaplic	num(8)		descr "Número de Comprobante"
							not null,
	rengaplic	num(3)		descr "Renglón"
							not null,
	importe		num(11,2)	descr "Monto"
							not null > 0,
	fecha		date		descr "Fecha de aplicación"
							not null,
	cdate		date		descr "Fecha de Carga",
	ctime		time		descr "Hora de Carga",
	cuid		num(5)		descr "Usuario que Cargo",
	mdate		date		descr "Fecha de Modificacion",
	mtime		time		descr "Hora de Modificacion",
	muid		num(5)		descr "Usuario que Modifico",

}primary key(numcomp, numord,aplic) [8],
index APLIC(tipaplic,nroaplic, rengaplic) [7];

table BOCPRA		descr "Beneficiario de Ordenes de Compra"
{
	numcomp		num(8)		descr "Número de Comprobante"
							not null,
	numord		num(8)		descr "Nro de Orden de Compra"
							in OCPRA(numcomp):valor
							not null,
	renglon		num(3)		descr "Renglón"
							not null,
	apynom		char(50)	descr "Apellido y Nombre / Empresa"
							mask "50>x"
							not null,
	tipdoc		char(3)		descr "Tipo de Documento"
							mask "3>x",
							// in gral.TIPDOC : descrip,
	nrodoc		num(8)		descr "Número de Documento",
	codcta		num(8)		descr "Código de cliente del Beneficiario"
							in CTADAT:(apynom),
	empleg		num(2)		descr "Empresa que autoriza beneficiario",
	nroleg		num(7)		descr "Nro de Legajo que autoriza beneficiario",
	emplegas	num(2)		descr "Empresa que asigna beneficiario",
	nrolegas	num(7)		descr "Nro de Legajo que asigna beneficiario",
	cdate		date		descr "Fecha de Carga",
	ctime		time		descr "Hora de Carga",
	cuid		num(5)		descr "Usuario que Cargo",
	mdate		date		descr "Fecha de Modificacion",
	mtime		time		descr "Hora de Modificacion",
	muid		num(5)		descr "Usuario que Modifico",

}
primary key(numcomp,numord)[5],   // Base 100.000
unique index NRODOC (tipdoc not null, nrodoc not null, numcomp, numord)[8];

table OCPRA			descr "Ordenes de Compra"
{
	numcomp		num(8)		descr "Número de Comprobante"
							in HOCPRA:codcta
							not null,
	numord		num(8)		descr "Nro de Orden de Compra"
							not null,
	valor		num(11,2)	descr "Monto"
							not null default 0.0,
	saldo		num(11,2)	descr "Saldo"
							not null default 0.0,
	impresa		bool		descr "Impresa S/N"
							not null
							default false,
	estado		num(2)		descr "Estado"
							not null,
	bloqueo		bool		descr "Bloqueo ?"
							not null
							default true,
	renglon		num(3)		descr "Renglón"
							not null,
	cdate		date		descr "Fecha de Carga",
	ctime		time		descr "Hora de Carga",
	cuid		num(5)		descr "Usuario que Cargo",
	mdate		date		descr "Fecha de Modificacion",
	mtime		time		descr "Hora de Modificacion",
	muid		num(5)		descr "Usuario que Modifico",

}primary key(numcomp,numord)[17],  // Base 1.000.000
unique index NROOCC(numord)[11];


table OCBLOQ			descr "Registración de bloqueos/no bloqueos de OC"
{
	nroreg		num(8)		descr "Número de Registración"
							not null,
	bloqueo		bool		descr "Bloqueo?"
							not null,
	codcta		num(8)		descr "Código de Cuenta",
	numcomp		num(8)		descr "Número de Comprobante",
	numord		num(8)		descr "Nro de Orden Compra",
	nroleg		num(7)		descr "Nro de Legajo"
							check digit "-",
	sucursal	num(3)		descr "Nro de Sucursal"
							not null,
	cdate		date		descr "Fecha de Carga",
	ctime		time		descr "Hora de Carga",
	cuid		num(5)		descr "Usuario que Cargo",
	mdate		date		descr "Fecha de Modificacion",
	mtime		time		descr "Hora de Modificacion",
	muid		num(5)		descr "Usuario que Modifico",
}
primary key(nroreg);

table RCTACTE	descr "Relación Cliente / Cuenta Corriente "
{

	codcta		num(8)		descr "Código de Cuenta"
							not null,
	tipccte		num(2)		descr "Tipo de Cuenta Corriente Asociada"
							not null,
	nroccte		num(6)		descr "Cuenta Corriente"
							not null,
	limauto		num(11,2) 	descr "Limite Autorizado"
							default 0.0,
	diasvenc	num(2)		descr "Dias de vencimiento",
	emp			num(2)		descr "Empresa",
	nroleg		num(7)		descr "Nro de Legajo",
    saltot      num(11,2)   descr "Saldo Total",
    salpend     num(11,2)   descr "Saldo Pendiente",
	activo		bool		descr "Activo"
							not null
							default true,
	tpagebs		num(3)		descr "Codigo de Término de Pago para EBS",
							//in CODIFICA (336)
	nroseq		num(9)		descr "Numero de secuencia para EBS",
	renglon		num(4)		descr "Renglon de direccion EBS",
	cdate		date		descr "Fecha de Carga",
	ctime		time		descr "Hora de Carga",
	cuid		num(5)		descr "Usuario que Cargo",
	mdate		date		descr "Fecha de Modificacion",
	mtime		time		descr "Hora de Modificacion",
	muid		num(5)		descr "Usuario que Modifico",

}
primary key (codcta, tipccte) [2], /* Base 20.000 */
index CCTE(tipccte,nroccte)   [2], /* Base 20.000 */
index NROSEQ(nroseq, renglon) [2]; /* Base 20.000 */

table GATRIB	descr "Atributos del Catálogo"
{

	atrib		char(30)	descr "Atributo"
							not null,
	descrip		char(30)	descr "Descripción"
							not null,
	tipo		num(1)		descr "Tipo"
							// in ventas.codifica (86)
							not null,
	valfijo		bool		descr "Valor Fijo"
							not null
							default false,
	descor		char(15)	descr "Descripcion Corta"
							not null,
	esinterno	bool		descr "Es atributo interno"
							not null
							default true,

}
primary key(atrib, esinterno);

table GVATRIB	descr "Valores Fijos por Atributo"
{

	atrib		char(30)	descr "Atributo"
							not null,
	valor		char(25)	descr "Valor Fijo"
							not null,
	activo		bool		descr "Activo"
							not null
							default true,
	umedida		num(2)		descr "Unidad de Medida",
							/* in ventas.codifica(91) */
	esinterno	bool		descr "Es atributo interno"
							not null
							default true,

}
primary key(atrib, esinterno, valor),
unique index ACTIVO(activo, atrib, esinterno, valor);

table GATRFAM	descr "Atributo por Familia"
{

	codflia		char(8)		descr "Código de Familia"
							not null,
							// in Ventas.familia,
	atrib		char(30)	descr "Atributo"
							not null,
	defval	 	char(25)	descr "Valor Fijo por Default",
	oblig		bool		descr "Obligatorio"
							not null
							default true,
	umedida		num(2)		descr "Unidad de Medida",
							/* in ventas.codifica(91) */
	subflia		num(2)		descr "Sub-Familia"
							not null
							default 0,
	esinterno	bool		descr "Es atributo interno"
							not null
							default true,

}
primary key(codflia, atrib, esinterno);

table GATRCAT	descr "Atributo por Artículo"
{

	codadm		char(8)		descr "Código de Artículo"
							not null,
							// in Ventas.familia;
	atrib		char(30)	descr "Atributo"
							not null,
	valor	 	char(25)	descr "Valor",
	umedida		num(2)		descr "Unidad de Medida",
							/* in ventas.codifica(91) */
	esinterno	bool		descr "Es atributo interno"
							not null
							default true,
	muid		num(5)		descr "Usuario que Modificó",
	mdate		date		descr "Fecha de Modificación",
	mtime		time		descr "Hora de Modificación",

}
primary key(codadm, atrib, esinterno)[13],
index ATRIB(atrib, esinterno, codadm)[13];

table FERIADOS	descr "Días Feriados"
{
	tipo		num(2)		descr "Tipo de Feriado"
							not null,
							// in ventas.codifica(90)
	fecha		date		descr "Fecha del Feriado"
							not null,
	descrip		char(20)	descr "Descripción"
							not null,
}
primary key (tipo, fecha);

table REIMPREM	descr "Reimpresion de Remitos"
{
	carpor		num(8)		descr "Número de Carta de Porte"
							not null,
	tipent		num(4)		descr "Tipo de comprobante"
							not null,
	compent		num(8)		descr "Número del comprobante"
							not null,
	renglon		num(3)		descr "Renglón"
							not null
							default 0,
	reimp		char(1)		descr "Tipo de Reimpresión"
							in ("A":"Asignación de Nro. de Remitos",
								"R":"Reimpresión")
							not null,
	sucursal	num(3)		descr "Sucursal o Depósito"
							not null,
	cdate		date		descr "Fecha de Carga",
	ctime		time		descr "Hora de Carga",
	cuid		num(5)		descr "Usuario que Cargo",

}primary key(carpor, tipent, compent, renglon),
index FECHA(cdate, sucursal);

table MODGRAL	descr "Modificación Generales por Usuario"
{
	tipomov		num(4)		descr "Tipo de comprobante"
							not null,
	numcomp		num(8)		descr "Número del comprobante"
							not null,
	renglon		num(3)		descr "Renglón"
							not null
							default 0,
	tipo		num(2)		descr "Tipo de Modificación"
							/*in ventas.codifica(93) */
							not null,
	fecha		date		descr "Fecha de Carga",
	hora		time		descr "Hora de Carga",
	userid		num(5)		descr "Usuario que Cargo",
    deporig		num(3)		descr "Depósito Origen",
    depodes		num(3)		descr "Depósito Destino"

}primary key(tipomov, numcomp, renglon),
index FECHA(fecha);


table CHRUTA				descr "Consolidado de Hoja de Ruta"
{
	nrocons		num(8)		descr "Nro. de Consolidado de Hojas de Ruta"
								not null,
	nrohoja		num(8)		descr "Nro. de Hoja de Ruta"
								not null,
	estado		char(1)		descr "Estado"
								in("O": "Original",
								   "R": "Reintento"),
	fecha		date		descr "Fecha de Consolidado",
	hora		time		descr "Time",
	usruid		num(5)		descr "Usuario que Cargo",
	esthruta    char(1)     descr "Estado de la Hoja de Ruta",
}primary key(nrocons, nrohoja)[17],    //Base: 1.000.000
index HRUTA(nrohoja, nrocons)[17],
index ESTADO(estado,fecha,nrocons)[15];

table HRTCAB 				descr "Hojas de Rutas de Transferencias"
{

	nrohoja		num(8)		descr "Nro. Hoja de Ruta"
							not null,
	sucursal	num(3)		descr "Sucursal que la originó"
							not null,
	fecha		date		descr "Fecha"
							not null,
	trans		num(9)		descr "Número de Transportista"
							not null,
	ctrl		char(20)	descr "Persona que carga el camión",
	patente		char(7)		descr "Patente del vehículo",
	precinto	char(30)	descr "Precintos",
	chofer		char(20)	descr "Nombre del Chofer",
	codobs		num(4)		descr "Cód. Observación",
	nroobs		num(8)		descr "Nro. Observación",
	valort		num(11,2)	descr "Valor Total",
	plus		num(11,2)	descr "Plus",
	sucdes		char(30)	descr "Sucursales destinos",
	estado	 	bool		descr "Confirmada s/n"
							default false not null,
	cdate		date		descr "Fecha de Carga",
	ctime		time		descr "Hora de Carga",
	cuid		num(5)		descr "Usuario que Cargo",
	plusbto		num(8)		descr "Plus Bulto",
	fconfi		date		descr "Fecha de confirmacion de datos.",
	hconfi		time		descr "Hora de confirmacion de datos.",
	uconfi		num(5)		descr "Usuario que confirmo los datos.",
	jaula		char(6)		descr "Nro. de Jaula",
	seguro		num(9)		descr "Compañia de Seguro",
	comfle		num(2)		descr "Medio de Comunicacion del Fletero",
	ncomfle     char(15)    descr "Numero de Medio de Comunicacion del Fletero",
	mentrega	char(1)		descr "Modo de entrega"
							mask ">A"
							in ("D":"Domiciliarias",
								"E":"Express",
								"T":"Venta telefonica interior",
							    "N":"No valorizada"),
	fecdes		date		descr "Fecha de despacho.",
	despacho	char(20)	descr "Despacho",
	acompan		char(20)	descr "Acompaniante del Fletero",
	sucdest		num(3)		descr "Sucursal de destino",
}primary key(nrohoja),
unique index FECHA(fecha,nrohoja),
index FCONFI(fconfi,nrohoja);

table HRTDET				descr "Hojas de Rutas de Transferencias"
{
	nrohoja		num(8)		descr "Nro. Hoja de Ruta"
							not null,
	tipomov		num(2)		descr "Tipo de Movimiento"
							not null,
	numcomp		num(8)		descr "Número de Comprobante"
							not null,
	codobs		num(4)		descr "Cód. de Observación",
	nroobs		num(8)		descr "Nro. de Observación",
	valor		num(11,2)	descr "Valorización",
	cant		num(5)		descr "Cantidad de Bultos",
	destino		num(3)		descr "Destino",
	estado	 	bool		descr "Confirmada s/n"
							default false not null,
	estref		char(1)		descr "Estado del Cbte. de Ref."

}primary key(nrohoja, tipomov, numcomp) [6],
index NROCOMP(tipomov, numcomp) [4];

table PROXART				descr "Valores por articulo a futuro" {

	tipo		num(3)		descr "Tipo de Valor"
							//in ventas.TIPVAL : descrip
							not null,
	suc			num(3)		descr "Sucursal"
							//in ventas.SUCURSAL : descor
							not null,
	codadm		char(8)		descr "Código de administración"
							//in ventas.CATALOGO : des1adm,
							not null,
	fdesde		date		descr "Fecha desde"
							not null,
	fhasta		date		descr "Fecha hasta",
	valor		num(10,2)	descr "Valor"
							not null,
	proc		bool		descr "Ya proceso"
							not null default false,
	cdate		date		descr "Fecha de Carga",
	ctime		time		descr "Hora de Carga",
	cuid		num(5)		descr "Usuario que Cargo",
	mdate		date		descr "Fecha de Modificacion",
	mtime		time		descr "Hora de Modificacion",
	muid		num(5)		descr "Usuario que Modifico",
}
primary key (tipo, suc, codadm, fdesde)[8],    // Base : 100.000 reg
index FECHA(fdesde, tipo, suc, codadm)[8],
index PROCES(fdesde, tipo, proc)[6];


table GPRESUP				descr "Mantenimiento de Presupuesto" {

	tipomov 	num(2)		descr "Tipo de Presupuesto"
							//in ventas.TIPOMOV(78)
							not null,
	nropres		num(8)		descr "Número de Presupuesto"
							not null,
	fecha		date		descr "Fecha de Emisión"
							not null
							default today,
	hora		time		descr "Hora de Registración del Movimiento"
							default hour,
	sucursal	num(3)		descr "Sucursal que genera el movimiento"
							//in ventas.SUCURSAL : descor
							not null
							default $div,
	codprov		num(6)		descr "Código de Proveedor"
							//in aurus CCTES(emp, tccte)
							not null,
	codcta		num(8)		descr "Cód. del cliente"
							in CTADAT:(apynom)
							not null,
	tipdoc		char(3)		descr "Tipo de Documento"
							mask "3>x",
							// in gral.TIPDOC : descrip,
	nrodoc		num(8)		descr "Número de Documento"
							not null,
	monto		num(11,2)	descr "Monto del presupuesto"
							not null
							default 0,
	montoaut	num(11,2)	descr "Monto Autorizado"
							not null
							default 0,
	tipoiva		 num(2)		descr "Tipo de Iva del Cliente."
							//in TIVA by VENTAS(true) : descrip
				      		default 1,
	vendedor	num(4)		descr "Vendedor que realiza la operación"
							//in ventas.VENDEDORES : vendnom
							not null,
	estado		num(2)		descr "Estado"
							//in ventas.CODIFICA (98)
							default 1
							not null,
	iva			num(11,2)	descr "Iva de N. Vta."
							default 0,
	bconac      num(2)		descr "Es cliente Bco. Nación"
							not null,
    diasval     num(4)		descr "Dias de Validez",
    faprob		date		descr "Fecha de Aprobación",
	dtoesp		num(5,2)	descr "Porc. de descto. especial"
							default 0,
	valdtoesp 	num(11,2)	descr "Valor del descto. especial"
							default 0,
	codctarel	num(8)		descr "Codigo de cliente relacionado",
	cdate		date		descr "Fecha de Carga",
	ctime		time		descr "Hora de Carga",
	cuid		num(5)		descr "Usuario que Cargo",
	mdate		date		descr "Fecha de Modificacion",
	mtime		time		descr "Hora de Modificacion",
	muid		num(5)		descr "Usuario que Modifico",

}primary key   (tipomov, nropres)[4],                 // Base : 100.000
index ESTADO  (sucursal, tipomov, estado, nropres)[5],
index SUCURSAL (sucursal, tipomov,nropres)[4],
index CLIENTE (codcta, tipomov, nropres)[6];

table GPRESART				descr "Artículos del Presupuesto" {

	tipomov 	num(2)		descr "Tipo de Presupuesto"
							//in ventas.TIPOMOV
							not null,
	nropres		num(8)		descr "Número de Presupuesto"
							not null,
	renglon		num(2)		descr "Número de renglón"
							not null,
	codadm		char(8)		descr "Artículo"
							not null,
	cantidad	num(9)		descr "Cantidad"
							not null,
	bruto		num(11,2)	descr "Bruto"
							default 0,
	neto		num(11,2)	descr "Neto"
							default 0,
	recargo		num(11,2)	descr "Recargo"
							default 0,
	descuento	num(11,2)	descr "Descuento"
							default 0,
	iva			num(11,2)	descr "Iva"
							default 0,
}primary key  (tipomov, nropres,renglon)[5];  // base 150.000

table GPRESMP				descr "Presupuesto por Medio de Pago" {

	tipomov 	num(2)		descr "Tipo de Presupuesto"
							//in ventas.TIPOMOV
							not null,
	nropres		num(8)		descr "Número de Presupuesto"
							not null,
	renglon		num(2)		descr "Número de renglón"
							not null,
	tipo		num(2)		descr "Tipo de Medio de Pago"
							//in ventas.MEDIOSP : descor
							not null,
	subm		num(3)		descr "Submedios de Pago"
							//in ventas.SUBMEDIOS (tipo) : descor
							not null,
	cuota		num(2)		descr "Cantidad de Cuotas"
						   //in ventas.CONDMP (tipo, subm) : (descto, tasa)
							not null,
	fecha		date		descr "Fecha de la Nota de Venta"
							default today
							not null,
	total		num(10,2)	descr "Total del M. Pago"
							not null,
	capital		num(10,2)	descr "Importe M. de Pago"
							not null,
	rec			num(10,2)	descr "Recargo del Medio de Pago"
							not null,
	descto		num(10,2)	descr "Descuento del M. Pago"
							not null,
}
primary key (tipomov, nropres, renglon)[4];   // base 100.000

table GPRESREL				descr "Relación Presupuesto / Comprobante" {

	tipomov 	num(2)		descr "Tipo de Presupuesto"
							//in ventas.TIPOMOV(78)
							not null,
	nropres		num(8)		descr "Número de Presupuesto"
							not null,
	tiprel	 	num(2)		descr "Tipo de Comprobante Asociado"
							//in ventas.TIPOMOV(9)
							//in ventas.TIPOMOV(2)
							not null,
	nrorel		num(8)		descr "Número de Comprobante"
							not null,

}primary key   (tipomov, nropres, tiprel),
unique index TIPREL  (tiprel, nrorel);

table LOTEGAR				descr "Lotes para Garantia Extendida" {
	nrolote		num(8)		descr "Numero de Lote"
							not null, // Serie SERNUM 2004
	tipolote	num(2)		descr "Tipo de Lote"
							not null, // in CODIFICA (100)
	fdesde		date		descr "Fecha Desde procesamiento",
	fhasta		date		descr "Fecha Hasta procesamiento" ,
	tgarfabd	num(4)		descr "Tiempo de Garantia Fabricante Desde",
	tgarfabh	num(4)		descr "Tiempo de Garantia Fabricante Hasta",
	fbase		date		descr "Fecha Dde Vto. Gar. Fabricante",
	dater		date		descr "Fecha de Creacion REAL",
	timer		time		descr "Hora de Creacion REAL",
	uidr		num(5)		descr "Usuario de Creacion REAL",
	dated		date		descr "Fecha de Creacion DEPURA",
	timed		time		descr "Hora de Creacion DEPURA",
	uidd		num(5)		descr "Usuario de Creacion DEPURA",
	nvi         num(8)		descr "Numero de Nota de Venta",
	dated7		date		descr "Fecha de Creacion DEPURA 7",
	timed7		time		descr "Hora de Creacion DEPURA 7",
	uidd7		num(5)		descr "Usuario de Creacion DEPURA 7",
	dated8		date		descr "Fecha de Creacion DEPURA 8",
	timed8		time		descr "Hora de Creacion DEPURA 8",
	uidd8		num(5)		descr "Usuario de Creacion DEPURA 8",
	dated9		date		descr "Fecha de Creacion DEPURA 9",
	timed9		time		descr "Hora de Creacion DEPURA 9",
	uidd9		num(5)		descr "Usuario de Creacion DEPURA 9",
	dated10		date		descr "Fecha de Creacion DEPURA 10",
	timed10		time		descr "Hora de Creacion DEPURA 10",
	uidd10		num(5)		descr "Usuario de Creacion DEPURA 10",
	dated11		date		descr "Fecha de Creacion DEPURA 11",
	timed11		time		descr "Hora de Creacion DEPURA 11",
	uidd11		num(5)		descr "Usuario de Creacion DEPURA 11",
	dated12		date		descr "Fecha de Creacion DEPURA 12",
	timed12		time		descr "Hora de Creacion DEPURA 12",
	uidd12		num(5)		descr "Usuario de Creacion DEPURA 12",
	dated13		date		descr "Fecha de Creacion DEPURA 13",
	timed13		time		descr "Hora de Creacion DEPURA 13",
	uidd13		num(5)		descr "Usuario de Creacion DEPURA 13",
	dated14		date		descr "Fecha de Creacion DEPURA 14",
	timed14		time		descr "Hora de Creacion DEPURA 14",
	uidd14		num(5)		descr "Usuario de Creacion DEPURA 14",
	dated15		date		descr "Fecha de Creacion DEPURA 15",
	timed15		time		descr "Hora de Creacion DEPURA 15",
	uidd15		num(5)		descr "Usuario de Creacion DEPURA 15",
	dated16		date		descr "Fecha de Creacion DEPURA 16",
	timed16		time		descr "Hora de Creacion DEPURA 16",
	uidd16		num(5)		descr "Usuario de Creacion DEPURA 16",
	dated1		date		descr "Fecha de Creacion DEPURA 1",
	timed1		time		descr "Hora de Creacion DEPURA 1",
	uidd1		num(5)		descr "Usuario de Creacion DEPURA 1",
	dated2		date		descr "Fecha de Creacion DEPURA 2",
	timed2		time		descr "Hora de Creacion DEPURA 2",
	uidd2		num(5)		descr "Usuario de Creacion DEPURA 2",
	dated3		date		descr "Fecha de Creacion DEPURA 3",
	timed3		time		descr "Hora de Creacion DEPURA 3",
	uidd3		num(5)		descr "Usuario de Creacion DEPURA 3",
	dated4		date		descr "Fecha de Creacion DEPURA 4",
	timed4		time		descr "Hora de Creacion DEPURA 4",
	uidd4		num(5)		descr "Usuario de Creacion DEPURA 4",
	dated5		date		descr "Fecha de Creacion DEPURA 5",
	timed5		time		descr "Hora de Creacion DEPURA 5",
	uidd5		num(5)		descr "Usuario de Creacion DEPURA 5",
	dated6		date		descr "Fecha de Creacion DEPURA 6",
	timed6		time		descr "Hora de Creacion DEPURA 6",
	uidd6		num(5)		descr "Usuario de Creacion DEPURA 6",

}primary key   (nrolote);

table LOTEMGAR				descr "Movim. Lotes para Garantia Extendida" {
	nrolote		num(8)		descr "Numero de Lote"
							in LOTEGAR:(tipolote, fdesde, fhasta)
							not null,
	tipomov 	num(2)	    descr "Tipo de Movimiento"
						    not null,
						    // in ventas.TIPOMOV
	numcomp		num(8)	    descr "Número de Comprobante"
						    not null,
	renglon		num(3)		descr "Nro. de  Renglón"
							not null,
}primary key   (nrolote, tipomov, numcomp, renglon)[56],  // base=15.000.000 separ recom 80 pero se baja a 56
unique index tipomov(tipomov, numcomp, renglon, nrolote)[56];


table CBTEMOT				descr "Motivo por Comprobantes" {

	tipomov 	num(2)		descr "Tipo de Movimiento"
							not null,
							// in ventas.TIPOMOV
	numcomp		num(8)		descr "Número de Comprobante"
							not null,
	tipmot		num(3)		descr "Tipo de Motivos"
							not null,
	renglon		num(3)		descr "renglon"
							not null,
	codmot		num(2)		descr "Código de Motivo",
							// in ventas.CODIFICA (108)
	observ		char(60)	descr "Observaciones",
	emp			num(2)		descr "Empresa",
	nroleg		num(7)		descr "Nro de Legajo",
	cuid		num(5)		descr "Usuario que Cargo",
	cdate		date		descr "Fecha de Carga",
	ctime		time		descr "Hora de Carga",
	muid		num(5)		descr "Usuario que Modifico",
	mdate		date		descr "Fecha de Modificacion",
	mtime		time		descr "Hora de Modificacion",

}primary key(tipomov, numcomp, tipmot, renglon) [16],
unique index renglon (tipomov, numcomp, renglon, tipmot) [16];

table ZONAXCAN				descr "Zonas por Cantidad" {

	zona		char(1)		descr "Codigo de zona"
							not null
							mask "1>A",
	cantidad	num(9)		descr "Cantidad"
							not null
							>= 0,
	minflete	num(11,2)	descr "Comisión de Flete"
	 						not null
	 						default 0,

}primary key(zona, cantidad);

table RELFIELD                descr "Relacion de rpfield con un atributo" {

	reporte		char(10)		descr "Nombre Reporte"
								not null,
	camporp     char(10)		descr "Nombre Campo"
								not null,
	atributo    char(30)        descr "Atributo"
								not null,
	esinterno	bool			descr "Es atributo interno"
								not null
								default true,
}primary key (reporte, atributo, esinterno);

table REGSEQ				descr "Sequencia y estado de registracion de REGCEL" {

	seq_id		num(10)		descr	"Nro. secuencia en ORACLE (REGISTRO_CELULAR)"
									not null,
	codresp		num(2)		descr	"Codigo de respuesta de ACTICEL"
									not null,
									//in CODIFICA(364)
	descresp	char(30)	descr	"Respuesta de Error de ACTICEL",
	nvta		num(8)		descr	"Numero Nota de Venta del REGCEL asociado"
									not null,
	renglon		num(2)		descr	"Renglon de Nota de Venta del REGCEL asociado"
									not null,
}primary key	(seq_id),
 index CODRESP	(codresp);

table CTAUIF				descr "Datos del Cliente para UIF" {
	codcta		num(8)		descr	"Codigo de Cliente"
									not null,
	coddir      num(4)      descr "Codigo de Direccion",
	pais		num(2)		descr "Codigo de pais de Nacimiento"
									not null,
	prov		char		descr "Codigo de provincia de Nacimiento",
	sexo		num(3)		descr "Sexo cliente"
									not null,
	cuil		char(15)	descr "C.U.I.L del cliente"
}
primary key		(codcta, coddir);



/****************************************************************************/
/*                                                                          */
/* No Agregar Tablas a este SCHEMA, la idea es utilizar el schema vengral,  */
/* el cual por naturaleza esta compartido por Garbarino y Compumundo        */
/* Su origen es multi-empresa, por lo tanto en caso de ser necesario se     */
/* debera incluir en la tabla nueva el campo "nroemp"                       */
/*                                                                          */
/****************************************************************************/

grant alter on schema serv to public;
grant all on * to public with grant option;

