/*  
* MODULO y VERSION : @(#)ventas.sc	10.45
* FECHA            : 13/04/26 15:11:16
* MODIFICACION     : 13/04/26 15:11:16
*/
/****************************************************************************/
/*                                                                          */
/* No Agregar Tablas a este SCHEMA, la idea es utilizar el schema vengral,  */
/* el cual por naturaleza esta compartido por Garbarino y Compumundo        */
/* Su origen es multi-empresa, por lo tanto en caso de ser necesario se     */
/* debera incluir en la tabla nueva el campo "nroemp"                       */
/*                                                                          */
/****************************************************************************/

schema ventas				descr "Esquema de ventas de GARBARINO"
							language "C";

table FAMILIA				descr "Familia de Artículos" {
	codflia		char(8)		descr "Código de familia"
							not null
							mask $codflia ,
	des1flia	char(20)	descr "1ra descr. de familia"
							not null,
	des2flia 	char(20)	descr "2da descr. de familia",
	descuento	num(4,2)	descr "Descuento que se realiza a la familia"
							default 0,
	fecult		date		descr "Fecha ult. actualización y/o alta"
							default today,
	agrup		num(2)		descr "Agrupamiento de Familia"
							in CODIFICA (4) : descrip
							not null,
	activo		bool		descr "Flag de familia disponible para la vta."
							not null default true,
	presup		num(2)		descr "Agrupamiento presupuesto por sucursal"
							in CODIFICA (244):descrip,
	artcons		bool		descr "Flag de familia disponible para consulta de stock"
							not null default true,
	relaciona	bool		descr "Relaciona con una familia de otra empresa?"
	                        not null default false,
	cdate		date		descr "Fecha de Carga",
	ctime		time		descr "Hora de Carga",
	cuid		num(5)		descr "Usuario que Cargo",
	mdate		date		descr "Fecha de Modificacion",
	mtime		time		descr "Hora de Modificacion",
	muid		num(5)		descr "Usuario que Modifico",
}
primary key (codflia),
unique index AGRUP (agrup, codflia),
index DESFLIA (des1flia, codflia),
index PRESUP (presup not null, codflia);

table MARCA 				descr "Marca del artículo"  {
	marca		num(8)		descr "Número de la marca"
							not null
							primary key,
	descrip		char(25)	descr "Descripción de la Marca"
							not null,
	proveedor	num(9)		descr "Número del proveedor",
//							not null < 1000000,
	fecult		date		descr "Fecha ult. actualización y/o alta"
							default today,
	descor		char(12)	descr "Descripcion corta de la marca",
	tipccte		num(2)		descr "Tipo de Cuenta Corriente del Proveedor"
							not null
							default 1,
}
unique index ALFABET(descrip) [ 2 ],
index 		 FECHA  (fecult, marca),
unique index PROVEE (tipccte, proveedor, marca);

table TIPOMOV				descr "Tabla de los tipos de movimientos" {
	codigo		num(2)		descr "Código del movimiento"
							not null,
	descrip		char(30)	descr "Descr. del movimiento",
	reduc		char(5)		descr "Descr. reducida",
	efecstk		num(1)		descr "Efecto en el Stock (suma o resta)"
							between -1 and 1,
    permitido	char(1)     descr "Flag que indica si este tipo se puede cargar a mano o no"
			    mask  ">A"
							in ("N":"No permite ingreso Manual",
								"S":"Se permite ingreso manual"),
    ultnum	num(8)	    descr "Número del movimiento para este tipo"
							default 0 not null,
	psdeporig	num(2)		descr "Parámetro 1",
	pdepdest	num(2)		descr "Parámetro 2",
	psdepdest	num(2)		descr "Parámetro 3",
	fecult		date		descr "Fecha ult. actualización y/o alta"
							default today,
	efstkori	num(1)		descr "Efecto en el Stock Origen (suma o resta)"
							between -1 and 1,
	efstkdes	num(1)		descr "Efecto en el Stock Destino (suma o resta)"
							between -1 and 1,
	efrecori	num(1)		descr "Efecto en el Reconstructor Origen (suma o resta)"
							between -1 and 1,
	efrecdes	num(1)		descr "Efecto en el Reconstructor Destino (suma o resta)"
							between -1 and 1,
	signo  		num(1)		descr "Signo"
							between -1 and 1,
	solocons	bool		descr "Comprobante de Solo Consulta"
							not null default false,
	formvalor	num(2)		descr "Forma de Valorizar"
							in CODIFICA (165) : descrip,
	asicble		num(2)		descr "Asiento Contable"
							not null default 0
							in CODIFICA (166) : descrip,
	stkdesp		num(1)		descr "Efecto sobre stock de despachos"
							in (0: "Sin Efecto",
								1: "Suma",
								2: "Resta",
								3: "Aplica",
								4: "Ajusta"),
	enidx		num(1)		descr "El tipomov esta en Ideafix ?"
							default 1
							not null 								
							in (1:"Ideafix RW", 2:"Oracle RW", 3:"Ambas c/Lectura s/Idx", 4:"Ambas c/Lectura s/Ora"),
}
primary key (codigo),
index PERM  (permitido, codigo);

table UMEDIDA				descr "Tabla de unidades de medidas" {
	codigo		char(3)		descr "Código de la unidad"
							not null,
	descrip		char(20)	descr "Descr. del código"

}
primary key (codigo);


table DEPOSIT 				descr "Tabla de depósitos" {
	codigo		num(3)		descr "Código del depósito"
							not null,
	descrip		char(20)	descr "Descrip. del depósito",
	descort		char(5)		descr "Descrip. corta del depósito"
}
primary key (codigo);

table SUBDEP    			descr "Tabla de subdepósitos"{
	codigo		num(2)		descr "Código del subdepósito"
							not null,
	descrip		char(20)	descr "Descrip. del subdepósito",
	descort		char(5)		descr "Descrip. corta del subdeposito",
	estado		num(1)		descr "Condición del Subdepósito",
	codrel      num(2)      descr "Subdepósito estado relacionado",
	reqaut      bool        descr "Requiere Autorizacion",
//	stkneg		bool		descr "Permite valores de stock negativo?"
//							default false,
}
primary key (codigo),
index ESTADO (estado, codigo);

table CATALOGO				descr "Catálogo de los Artículos en stock" {
	codadm		char(8)		descr "Código de administración"
							not null,
	des1adm 	char(25)	descr "1ra descr. del artículo"
							not null,
	des2adm 	char(20)	descr "2da descr. del artículo",
	codflia		char(8)		descr "Código de familia"
							not null
							in FAMILIA:(des1flia, des2flia),
	tgarfab		num(4)		descr "Tiempo de gar. fabricante",
							// not null,
	unidad  	char(3) 	descr "Unidad de medida"
							in UMEDIDA:(descrip) ,
	codadmpro	char(14)	descr "Número de artículo del proveedor",
	codiva		num(2)		descr "Código de iva"
							not null,
    codimpi		num(1)		descr "Código de impuestos internos"
							not null,
	tipo		char(1)		descr "Tipo de bien"
							in ("A": "Artículo",
								"G": "Garantia Extendida",
								"S": "Servicio",
								"P": "Poliza de Seguro")
							mask ">A"
							default "A",
	codcta		num(4)		descr "Código de Cuenta",
	provunico	num(8)		descr "Código del proveedor único (si existe)"
							in MARCA:(descrip),
	margen1     num(5,2)	descr "Margen de ganancia del Artículo lista 1"
							default 0,
	margen2     num(5,2)	descr "Margen de ganancia del Artículo lista 2"
							default 0,
	costoval	num(10,2)	descr "Costo valorizado"
							default 0,
	costovant	num(10,2)	descr "Costo valorizado anterior"
							default 0,
	costoult	num(10,2)	descr "Costo de Ultima Compra"
							default 0,
	costoant	num(10,2)	descr "Costo Anterior"
							default 0,
	costorep	num(10,2)	descr "Costo de reposición"
							default 0 not null,
	fcostoval	date		descr "Fecha de costo valorizado",
	fcostovant	date		descr "Fecha de costo valorizado anterior",
	fcostoult	date		descr "Fecha de costo último",
	tcostoult	time		descr "hora ultima modificacion precio2",
	uidcostoult	num(4)		descr "Número del usuario que modifico el precio",
	fcostorep	date		descr "Fecha de costo reposición",
	tcostorep	time		descr "hora última modificacion precio2",
	uidcostorep	num(4)		descr "Número del usuario que modifico el precio",
    activo	char(1)     descr "Flag de artículo disponible para la vta."
							in ("N":"No esta disponible a la venta",
								"S":"Disponible para la venta")
							not null
							default "N"
							mask ">A",
	precio1     num(10,2)	descr "Precio de venta minorista con iva"
							default 0
							not null,
	fprecio1	date		descr "Fecha última modificación precio1",
	tprecio1	time		descr "Hora última modificación precio1",
	uidprecio1	num(4)		descr "Número del usuario que modifico el precio",
	precio2		num(10,2)	descr "Precio de venta minorista sin iva"
							default 0,
	fprecio2	date		descr "precio última modificación precio2",
	tprecio2	time		descr "hora última modificación precio2",
	uidprecio2	num(4)		descr "Número del usuario que modificó el precio",
	comart      num(5,2)    descr "Porc. aumento sobre la Comisión del Vendedor (vta. ctdo.)"
							not null
							default 0,
	descuento	num(4,2)	descr "Descuento que se realiza al artículo"
							default 0,
	fecact		date		descr "fecha de última act. o alta "
							default today,
	comcre		num(5,2)	descr "Porc. aumento sobre la Comisión del Vendedor (vta. créd.)"
							not null
							default 0,
	comfija		num(9,2)	descr "Comisión fija del artículo"
							not null
							default 0,
	estact		bool		descr "Estado del art. activo ?"
							default true not null,
	comisid		char(2)		descr "Identif. de Comisión",
	ctaord		num(2)		descr "A Cuenta y Orden",
							// in aux.CTAYORD:descrip
	grupart		num(2)		descr "Grupo de Artículos"
							in CODIFICA (56) : descrip,
	tipesc		num(2)		descr "Tipo de Escala de Celulares",
	tipgar		char(1)		descr "Tipo de Garantía",
	tobsana		num(4)		descr "Tipo de Observación",
							//in ventas.sernum
	obsana		num(8)		descr "Numero de Observacion",
							// in serv.obsgral (tobsana)
	categ		num(2)		descr "Categoría",
							// in CODIFICA(101)
	codmonpv	num(2)		descr "Codigo de Moneda PVta",
	tipvalz		num(2)		descr "Tipo de Valorizacion", // in CODIFICA(146)
	calcii      num(1)      descr "Calculo Impuestos Internos"
								in (0: "No aplica",
								    1: "(Importamos directamente)Aplica solamente en la Venta",
								    2: "(Comprado en el pais)Aplica solamente en la Compra")
								not null 
								default 0,
	origen		num(2)		descr "Origen"
							in CODIFICA(171),
	asocia		bool		descr "Asocia otro artículo?"
							default false not null,
	tipcomp     num(2)      descr "Tipo de Compra"
							in CODIFICA (212)
							not null
							default 1,
	codmerco	char(13)	descr "Codigo Mercosur",
	tipseg      num(2)      descr "Tipo de Seguro"
							in CODIFICA (283),
	tvtaseg		num(5)		descr "Tiempo Venta de Seguro (Dias)",
	actcomp		num(2)		descr "Activo para la Compra?"
							in CODIFICA (323),
	altaext		num(1)		descr "Alta dada externamente", 
	cdate		date		descr "Fecha de Carga",
	ctime		time		descr "Hora de Carga",
	cuid		num(5)		descr "Usuario que Cargo",
	mdate		date		descr "Fecha de Modificacion",
	mtime		time		descr "Hora de Modificacion",
	muid		num(5)		descr "Usuario que Modifico",

}
primary key (codadm) [2], /*Base 15.000 */
index CODFLIADM (codflia,codadm) [4],
index ALFAB (provunico, des1adm, des2adm, codadm) [11],
index MARCOD		(provunico, codadm) [3],
index FLIAMCOD		(codflia, provunico, des1adm, des2adm, codadm) [13],
index ACTIVO        (activo,codadm)[3],
index TIPVALZ       (tipvalz not null, codadm)[3],
index CATEG         (categ, codadm)[3];

table STOCK 				descr "Tabla de existencias de Stock" {
	codadm		char(8)		descr "Código de administración"
							in CATALOGO:(des1adm)
							not null
							mask $codadm,
    deposito	num(3)		descr "Depósito"
							not null
							in DEPOSIT:(descrip),
	subdep		num(2)		descr "Subdepósito"
							not null
							in SUBDEP:(descrip),
	stkact		num(8)  	descr "Stock actual en unidades"
	  						not null default 0,
    fecult      date    	descr "Fecha del último movimiento",
    mtime		time		descr "Hora de Modificacion",
}
primary key (codadm,deposito,subdep) [ 7 ],
index DEPOSITO (deposito,subdep,codadm) [ 7 ],
unique index CODSUB (codadm, subdep, deposito) [ 7 ];

table MOVIM  				descr "Movimientos de artículos de Stock"  {
	tipomov 	num(2)		descr "Tipo de movimiento de Stock"
							in TIPOMOV
							not null,
	numcomp		num(8)		descr "Número de comprobante"
							not null,
	codadm 		char(8)		descr "Código del artículo de Stock movido"
							in CATALOGO
							not null,
	renglon		num(3)		descr "Número de renglón"
							not null
							default 0,
	fecha		date		descr "Fecha de registración del movimiento"
							not null,
	sucursal	num(3)		descr "Sucursal que genera el movimiento"
							in SUCURSAL : descor
							default $div,
	codorig 	num(3)		descr "Depósito de origen del movimiento"
							in DEPOSIT ,
	scodorig 	num(2)		descr "Estado origen del movimiento"
							in SUBDEP,
	coddest 	num(3)		descr "Depósito de destino del movimiento"
							in DEPOSIT,
	scoddest 	num(2)		descr "Estado de destino del movimiento"
							in SUBDEP,
	cliente		num(9)		descr "Cód. del cliente",
	cantidad	num(9)		descr "Cantidad de unidades del art movidas"
							not null,
	costo		num(11,2)	descr "Costo unitario del artículo movido",
	cantf		num(9)		descr "Cantidad facturadas"
							default 0,
	cante		num(9)		descr "Cantidad Entregada"
							default 0,
	cantd		num(9)		descr "Cantidad Devuelta"
							default 0,
	oper		char(2)		descr "Tipo de Operación Retira/Envia (N.V)",
//							mask ">A",
	empresa		num(2)		descr "Empresa en la que se registra el movimiento",
	vendedor	num(4)		descr "Vendedor que realiza la operación"
							in VENDEDORES : vendnom,
	fecent		date		descr "Fecha de entrega"
							default null,
	horent		time		descr "Hora de entrega",
	tipent		num(4)		descr "Tipo de comprobante que cancela"
							default null,
	compent		num(8)		descr "Número del comprobante que cancela"
							default null,
	renent		num(3)		descr "Renglón del Comprobante que cancela"
							default null,
	tipdocref	num(2)		descr "Tipo de Documento de referencia"
							in TIPOMOV,
	docref 		num(8)		descr "Documento de Referencia del movimiento de Stock",
	operador	num(4)		descr "Operador que valida la Operación",
	fecultmod	date		descr "Fecha de última modificación"
							default today,
	horamod		time		descr "Hora de última modificación"
							default hour,
	nrocomp		num(9)		descr "Guarda el Nro de Fact. de Referencia"
							default null,
	zona		char(1)		descr "Zona (para reparto)"
							mask "1>x"
							in ZONAS : (descrip),
	descto		num(11,2)	descr "Descto. aplicado al mov."
							default 0,
	comis		num(11,2)	descr "Comision"
								default 0,
	iva			num(11,2)	descr "Iva"
								default 0,
	ivaadic		num(11,2)	descr "Iva adicional"
								default 0,
	preultcom	num(11,2)	descr "Precio Ultima Compra"
								default 0,
	estado		num(2)		descr "Estado",
	hora		time		descr "Hora de Registración del Movimiento"
							default hour,
	flete		num(11,2)	descr "Flete"
							default 0,
	recargo		num(11,2)	descr "Recargo aplicado al mov."
							default 0,
	iva3337		num(11,2)	descr "Iva RG3337"
								default 0,

	impint		num(11,2)	descr "Impuestos Internos"
								default 0,
}
primary key   (tipomov,numcomp,codadm,renglon) [ 52 ],
index CODFEC  (codadm,fecha,hora,tipomov,numcomp,renglon) [ 50 ],
index ENTREGA (tipent not null,compent not null,tipomov,numcomp,codadm,renglon) [ 32 ],
index SUCFECH (fecha, tipomov, sucursal, numcomp, renglon) [ 38 ],
index ESTADO  (estado not null, tipomov, numcomp, renglon) [ 15 ];


table NOTVTA 				descr "Cabecera de Nota de Venta" {
	notvta		num(8)		descr "Número de nota de venta"
							not null,
	fecha		date		descr "Fecha de la Nota de Vta"
							default today,
	fefact		date		descr "Fecha de Facturación"
							default null,
	estado		char(1)		descr "Condición de aprobación de la N.V."
							in CODIFC(1):descrip
							default "A"
							mask "X",
	sucursal    num(3)      descr "Sucursal donde se realizo la venta"
							in SUCURSAL : descor,
	tipofac		char(1)		descr "Tipo de Factura"
							in ("A":"Fc. Tipo A",
								"B":"Fc. Tipo B",
								"E":"Fc. Tipo E")
							mask ">x",
	seriefac	char(4)		descr "Sucursal Nro."
							mask "4n",
	nrofac		char(8)		descr "Factura Nro."
							mask "8n",
	bruto		num(11,2)	descr "Bruto de N. Vta."
							default 0,
	neto		num(11,2)	descr "Neto de N. Vta.",
	flete		num(11,2)	descr "Flete de N. Vta."
							default 0,
	recargo		num(11,2)	descr "Recargo de N. Vta."
							default 0,
	descuento	num(11,2)	descr "Descuento de N. Vta."
							default 0,
	iva			num(11,2)	descr "Iva de N. Vta."
							default 0,
	iva1		num(11,2)	descr "Iva 1 de N. Vta."
							default 0,
	comision	num(11,2)	descr "Comision de la N. Vta."
							default 0,
	nombre	     char(30)   descr "Nombre del Cliente."
							not null,
	direccion	 char(30)	descr "Dirección del Cliente."
							not null,
	local		 char(30)	descr "Localidad del Cliente.",
	prov		char(1)		descr "Provincia"
							in PROVINCIAS : descrip
							not null
							mask ">x",
	cpostal		 num(4)		descr "Codigo postal",
	cuit         char(15)   descr "C.u.i.t. del Cliente.",
	tipoiva		 num(2)		descr "Tipo de Iva del Cliente."
							in 
							TIVA by VENTAS(true) : descrip
				      		default 1,
	tipdoc		 char(3)	descr "Tipo de Documento del Cliente.",
	nrodoc		 num (9)	descr "Nro. de Documento del Cliente.",
	area		char(5)		descr "Numero de DDN",
	telefono	char(8)		descr "Numero de telefono",
	fecultmod	 date 		descr "Fecha de última modificación"
							not null
							default today,
	horamod		time		descr "Hora de última modificación"
							not null
							default hour,
	dtoesp		num(5,2)	descr "Porc. de descto. especial"
							default 0
							not null,
	zona		char(1)		descr "Codigo de zona"
							mask "1>x"
							in ZONAS : (descrip),
	poriva		num(4,2)	descr "Porcentaje de Iva",
	poriva1		num(4,2)	descr "Porcentaje de Iva 1",
	flag		num(4)		descr "Flag",
	rg3337		bool		descr "Se le realiza RG3337"
							not null
							default false,
	iva3337		num(11,2)	descr "Iva RG3337"
							default 0,
	piva3337	num(4,2)	descr "Porcentaje de Iva RG3337",
	vendedor	num(4)		descr "Vendedor"
							not null
							in VENDEDORES : vendnom,
	discr		bool		descr "Discrimina Iva"
							not null default false,
	via			num(2)		descr "Via de Conocimiento"
							in CODIFICA (1) : descrip,
	codcli		num(8)		descr "Código de Cta. Cte. del Cliente",
	
	horacot		time		descr "Hora de la Cotizacion",
	feccot		date		descr "Fecha de Cotizacion",
	impint		num(11,2)	descr "Impuestos Internos"
							default 0,
}
primary key 		 (notvta) [8],  /*Base 500.000 */
unique index SUCURSAL(sucursal, estado, notvta) [11],     /*Base 500.000 */
unique index FECHA   (fefact, notvta) [10],  /*Base 500.000 */
unique index FACTURA (tipofac not null, seriefac not null, nrofac not null, notvta) [20]; /*Base 500.000 */

table NOTFIN 				descr "Financiacion Notas de Venta" {

	nvta		num(8)		descr "Número de nota de venta"
							in NOTVTA : fecha
							not null,
	renglon		num(2)		descr "Número de renglón"
							not null,
	tipo		num(2)		descr "Tipo de Medio de Pago"
							in MEDIOSP : descor
							not null,
	subm		num(3)		descr "Submedios de Pago"
							in SUBMEDIOS by tipmedio(tipo, 0) : descor
							not null,
	cuota		num(2)		descr "Cantidad de Cuotas"
							in CONDMP (tipo, subm) : (descto, tasa)
							not null,
	fecha		date		descr "Fecha de la Nota de Venta"
							default today
							not null,
//	El total incluye el capital, recargo y descto
	total		num(10,2)	descr "Total del M. Pago"
							not null,
	capital		num(10,2)	descr "Importe M. de Pago"
							not null,
	rec			num(10,2)	descr "Recargo del Medio de Pago"
							not null,
	descto		num(10,2)	descr "Descuento del M. Pago"
							not null,
	empresa		num(2)		descr "Empresa a la que pertenece",
//	nro de cheque, expren, prestamo
	nrocomp		num(8)		descr "Nro de comprobante"
							default null,
	nroauto		char(8)		descr "Numero de Autorizacion de Tarj"
							default null,
	sucursal    num(3)      descr "Sucursal"
							in SUCURSAL : descor
							not null,
	cajero		num(5)		descr "Cajero",
	operiso		num(1)		descr "Ultima Operacion (ISO)",
	pricred		num(2)		descr "Primera operacion de cred. pers./vip"
							not null
							default 0
							in CODIFICA(66):descrip,
	fecauto		date		descr "Fecha de autorización",

	horacot		time		descr "Hora de la Cotizacion",
	
	codmon		num(2)		descr "Moneda relacionada",
							// in aux.moneda
	mctetotal	num(10,2)	descr "Total del M. Pago en moneda corriente"
							not null
							default 0.0,
	mctecap		num(10,2)	descr "Importe M. de Pago en moneda corriente"
							not null
							default 0.0,
	mcterec		num(10,2)	descr "Recargo del Medio de Pago en moneda corriente"
							not null
							default 0.0,
	mctedto		num(10,2)	descr "Descuento del M. Pago en moneda corriente"
							not null
							default 0.0,
    feccot		date		descr "Fecha de Cotizacion",
	porcmax		num(5,2)	descr "Porc. Dto. Usuario",
	porcban		num(5,2)	descr "Porc. Dto. Comercio",
	promo		num(3)		descr "Promocion Bancaria"
}
primary key (nvta, renglon) [10], /*Base 600.000*/
unique index MEDIOS (fecha, sucursal, cajero, nvta, renglon) [17], /*Base 600.000 */
index TIPCUO (tipo, cuota, subm, fecha, nvta) [14] /* Base 600.000 */;

table LEYNVTA				descr "Leyenda para NOTAS DE VENTA" {
	notvta		num(8)		descr "Numero de Nota de Venta"
							in NOTVTA : fecha
							not null,
	renglon		num(2)		descr "Renglon de la Leyenda"
							not null,
	sucursal	num(3)		descr "Sucursal donde se realizo la venta"
							in SUCURSAL : descor
							not null,
	leyenda		char(30)	descr "Leyenda",
}
primary key(notvta, renglon) [6]; /*Base 200.000 */

table DESTNVTA				descr "Destino para NOTAS DE VENTA" {
	notvta		num(8)		descr "Numero de Nota de Venta"
							in NOTVTA : fecha
							not null,
	sucursal	num(3)		descr "Sucursal donde se realizo la venta"
							in SUCURSAL : descor
							not null,
	nombre		char(30)	descr "Nombre del Destinatario."
							mask "30>x",
	direc		char(30)	descr "Dirección del Destinatario."
							mask "30>x",
	local		char(30)	descr "Localidad del Destinatario."
							mask "30>x",
	area		char(5)		descr "Numero de DDN",
	telefono	char(8)		descr "Numero de telefono",
	prov		char(1)		descr "Provincia"
							in PROVINCIAS : descrip
							not null
							mask ">x",
	zona		char(1)		descr "Codigo de zona"
							mask "1>x"
							in ZONAS : (descrip),
}
primary key(notvta);

table VENDEDORES			descr "Tabla de Vendedores" {

	vendnum		num (4)		descr "Numero del Vendedor"
						 	      not null
                                  between 1 and 9999,
	vendnom     char(20)	descr "Nombre del Vendedor"
							      not null,
	vendcat		num (2)     descr "Categoria del Vendedor"
                                  in CODIFICA(3):descrip
                                  default 2
                                  not null,
    vendcom     num (5,3)   descr "Comision del Vendedor por Contado"
				    			  not null
                                  default 0.0
                                  between 0 and 99.999 ,
	vendcre		num (5,3)   descr "Comision del Vendedor por Credito"
                                  not null
                                  default 0.0
                                  between 0 and 99.999 ,
	activo		bool		descr "Activo"
								not null default true,
	comgar		num(2)		descr "Codigo de Comis. para Gar. Ext."
							in COMVGAR:descrip,
	sucursal	num(3)		descr "Código de Sucursal"
							in SUCURSAL by TIPSUC (true) : descor,
	complus	    num(11,2)	descr "Plus de Comisión"
							not null default 0.0,	    							
	emp         num(2)      descr "Código de Empresa",
	nroleg      num(7)      descr "Nro. de Legajo" 
							check digit "-",
    fecegr      date        descr "Fecha de Egreso (DENARIUS)",
   	regcom		bool		descr "Registra Comisión"
		  					not null default true,
	fecalta		date		descr "Fecha de Alta",

}

primary key	  (vendnum),
index CAT_VEN (vendcat, vendnum),
unique index NROLEG  (emp not null, nroleg not null),
unique index SUCVEN (sucursal, vendnum);

table NLIBRES				descr "Tabla de Numeros Libres por Tipo de Mov." {

	tipomov		num(2)		descr "Tipo de movimiento"
							not null
							in TIPOMOV : descrip,
	numero		num(8)	    descr "Número libre para este tipo"
							not null,
 	sucursal	num(3)		descr "Sucursal donde se realizo la venta"
							in SUCURSAL : descor
							not null,
	fecha		date		descr "Fecha"
							default today,
	estado		num(2)		descr "Estado del nro."
							in (1 : "en uso", 2 : "libre")
							not null,
	tty			char(8)		descr "Terminal",
	muid		num(5)		descr "Usuario",
	pid			num(6)		descr "Numero de Proceso",
	mdate		date		descr "Fecha de Modificacion",
	mtime		time		descr "Hora de Modificacion",
}
primary key (tipomov, numero) [ 3 ],
index SUCEST(tipomov, sucursal, fecha, estado, numero)[3];

table MEDIOSP				descr "Tabla de Medios de Pagos." {
	codigo		num(2)		descr "Código del Medio de Pago"
							not null,
	descrip		char(20)	descr "Descripcion del Medio."
							not null,
    descor		char(10)	descr "Descripcion corta."
							not null,
	nrocomp		bool		descr "Lleva Nro de comprobante"
							not null,
	nroauto		bool		descr "Lleva Numero de Autorizacion"
							not null,
	credito		bool		descr "Es Credito (para comisiones) NO SE USA"
							// Se utiliza la de CONDMP
							not null,
	activo		bool		descr "Activo"
							not null default true,
	impcomp		bool		descr "Imprime Comprobante"
							not null default false,
	impcuo		bool		descr "Imprime Valor de las Cuotas"
							not null default false,
	comvend		num(5,3)	descr "Comision del Vendedor (coef.)"
							between 0 and 99.999,
	difmes		num(1)		descr "Meses de difer. (Calculo del TAE)"
							not null default 0,
	diferido	bool		descr "Es Diferido?"
							default false
							not null,
	qdias		num(4)		descr "Cantidad de Días del MPago",
	tipcbte		num(2)		descr "Cupón o Tipo de Comprobante"
							in CODIFICA (84) : descrip,
	nrocbte		num(2)		descr "Código de Autorización"
							in CODIFICA (85) : descrip,
	oocc		bool		descr "Es Orden de Compra?"
							default false
							not null,
	refer		bool		descr "Referencia Obligatoria a Medios de Telefonica?"
							default false
							not null,
}
primary key (codigo);


table SUBMEDIOS				descr "Tabla de Sub - Medios de Pagos." {
	codigo		num(2)		descr "Código del Medio de Pago"
							in MEDIOSP : descrip,
	subcod		num(3)		descr "Desglose de los M. Pagos"
											// Tarjetas u otros
							not null,
	descrip		char(20)	descr "Descripcion del Sub - Medio."
							not null,
    descor		char(10)	descr "Descripcion corta del Sub - Medio."
							not null,
	moneda		num(2)		descr "Tipo de Moneda"
//							in (1 : "Dólares",
//								2 : "Pesos")
//							Paso a ser in aux.MONEDA
							not null,
	concepto 	num(2)		descr "Concepto Asociado en Aurus"
							not null,
	imputacion	num(2)		descr "Imputacion Asociada a Aurus"
							not null,
	tipcot		num(2)		descr "Tipo de Cotización",
	activo		bool		descr "Activo"
							not null default true,
	reqaut		bool		descr "Requiere autorizacion para Facturar"
							not null default false,
	reqx25		bool		descr "Requiere autor. via X25 (ISO) para Facturar"
							not null default false,
	tarjprop	bool		descr "Es Tarjeta Propia"
							not null default false,
	codnom 		num(2)  	descr "Código de Nombre del SubMedio"
							in CODIFICA(51):descrip,
	proyec		bool		descr "Se considera en la Proyeccion"
							not null default true,
	credpers	num(2)		descr "Es crédito personal/Vip/Incentivo"
							not null default 0
							in CODIFICA by VALOR(66,1):descrip,
	diasauto	num(2)		descr "Cantidad de Días para control de la fauto",
	tipmincr	num(2)		descr "Tipo de Código Mínimo Cred. Pers.",
	tpropia 	bool		descr "Es Tarjeta Propia Nueva"
							not null default false,
	afecta		bool		descr "Afecta Cuenta Corriente ?"
							not null default false,
	diasvenc	num(2)		descr "Cantidad de Días p/ Vencimiento" ,
	tipccte		num(2)		descr "Tipo de Cuenta Corriente Asociada",
							//in tcctes:(descrip),
	tipmedio	num(1)		descr "Es MP refinanciacion ?"
				   			not null
				   			in (0:"Venta", 1:"Refinanciacion")
							default 0,
							// Se declara como num y no bool por bug version
	estado		char(1)		descr "Estado en que queda la NVI"
							in CODIFC(1):descrip,
	tipolog		num(2)		descr "Tipo de log",
							// in ventas.CODIFICA(123)
	rol			num(2)		descr "Rol o Función",
							// in gmailing.grolsuc (emp, suc)
	subrol		num(2)		descr "Subrol o Función",
							// in en codifica (rol, rol.valor)
	clase		num(2)		descr "Clasificacion para el Valgas",
	cctos		bool		descr "Afecta a Centro de Costos?"
							not null default false,
	div			num(3)		descr "Divisisn a la que afecta",
							// in aurus.divs(emp)
	depto		num(4)		descr "Departamento al que afecta",
							// in aurus.divdep(emp, div)
	ingcob		bool		descr "Se puede ingresar en Autoriazcionesn de TP ?"
							not null default false,
	ingcob1		bool		descr "Se puede ingresar en cobranza de TP ?"
							not null default false,
	efvo		bool		descr "Efectivo s/n ? ",							
	tarjdeb		bool		descr "Es tarjeta de debito?"
							not null default false,
	plandep 	bool		descr "Entra en Planilla de Deposito de Caja ?",	
	reqauttp	bool		descr "Requiere autorizacion para ingresar en aut. TP ?"
							not null
							default false,		
	afecrent	bool		descr "Afecta Rentabilidad S/N"
							not null
							default false,							
	conc_nci	num(2)		descr "Concepto Asociado en Aurus p/NCI",
	impu_nci	num(2)		descr "Imputacion Asociada a Aurus p/NCI",
	tieneneg	num(2)		descr "Tiene Base de Negativos ?"
							in CODIFICA (182):descrip
							default 0,
	mediotel	num(2)		descr "Medio Pago de Venta Telefonica",
	submtel		num(2)		descr "Submedio de Venta Telefonica",
	fpago		char(4)		descr "Forma de Pago Altos",
	ctarj		char(4)		descr "Codigo de Tarjeta Altos",
	wtflow		num(2)		descr "Tipo de Workflow",
							//in gmailing.wftflow(wfemp):(wfdescr)
	moding		num(1)		descr "Modo de Ingreso"
							in(	0:"Por Banda",
								1:"Manual",
								2:"Ambas"),
	modoper	   num(1)		descr "Modo de Operacion"
							in( 0:"On-Line",
								1:"Off-Line",
								2:"Ambas"),
	ingsenia  	bool		descr "Permite ingreso en señas?"
							default true
							not null,
	altsuc		bool		descr "Circuito Alta Tarjeta en Sucursal?"
							default false
							not null,
	seccode		bool		descr "Requiere Código de Seguridad"
							default false
							not null,
	reqlreg		bool		descr "Requiere Lista de Regalos"
							default false
							not null,
	opertipo	num(1)		descr "Operacion en nota de venta"
							default 2
							not null
							in( 0:"Retira",
								1:"Entrega",
								2:"Ambos"),
	wfestini	num(3)		descr "Estado Inicio",
	wfestctr	num(3)		descr "Estado Controlador",
	montomin	num(10,2)	descr "Monto minimo p/pago con tarjeta",
	empleado	num(2)		descr "Medio de pago del empleado",
	prestamo	bool		descr "Acepta Prestamo ?"
							default false
							not null,
	medemps		char(2)		descr "Medio de Pago  Empresa Shopping",
	smedemps	char(2)		descr "SubMedio de Pago Empresa Shopping",
	cftext		char(40)	descr "Texto para controlador fiscal.",
	aplicnci	bool		descr "Aplica NCI en Arqueo ?"
							default false
							not null,
	capl_nci	num(2)		descr "Concepto Aplicacion NCI",   
	iapl_nci	num(2)		descr "Imputacion Aplicacion NCI",
	ccom_nvi	num(2)		descr "Concepto Compensacion NVI",   
	icom_nvi	num(2)		descr "Imputacion Compensacion NVI",
	ccom_nci	num(2)		descr "Concepto Compensacion NCI",   
	icom_nci	num(2)		descr "Imputacion Compensacion NCI",
	repite		bool		descr "Permite Repetir en Venta"
							default false
							not null,
	trxebs		num(3)		descr "Tipo de Transacción EBS"
							in CODIFICA (347),
	con_cont	num(2)		descr "Concepto de la contrapartida",
	imp_cont	num(2)		descr "Imputacion de la contrapartida",
	wfcodtar	num(3)		descr "WF - Codigo de Tarjeta",
	montomax	num(10,2)	descr "Monto maximo p/pago con tarjeta",
	entidad		num(3)		descr "Codigo de Entidad Bancaria",
//							in ddirect.ddenti

}
primary key (codigo, subcod),
unique index COTIZ (moneda, codigo, subcod),
unique index CODNOM(codnom, codigo, subcod),
unique index TIPMEDIO (codigo, tipmedio, subcod),
unique index EFVO(efvo not null, moneda),
unique index PLANI (plandep not null, codigo, subcod);

table CONDMP				descr "Condiciones de Sub - Medios de Pagos." {
	codigo		num(2)		descr "Código del Medio de Pago"
							in MEDIOSP : descrip,
	subcod		num(3)		descr "Desglose de los M. Pagos"
											// Tarjetas u otros
							in SUBMEDIOS(codigo) : descrip,
	cuotas		num(2)		descr "Cantidad de cuotas - pagos"
							not null
							> 0,
	descto		num(4,2)	descr "Descuento (%)"
							not null,
	tasa		num(4,2)	descr "T.I.M.D. (%)" // % int. mens. direct.
							not null,
	activo		bool		descr "Activo"
							not null default true,
	comisv		num(4,2)	descr "Comisión"  // No estaria en uso
							not null default 0,
	credito		bool		descr "Es Credito (para comisiones)"
							default true
							not null,
	tpagebs		num(3)		descr "Codigo de Término de Pago para EBS"
							in CODIFICA (336),
}
primary key (codigo, subcod, cuotas);


table DESCTOS				descr "Descuentos máximos para N. VTA." {

	nivel		num(2)		descr "Nivel Jerárquico" // var de ambiente
							not null,
	descrip		char(20)	descr "Descripcion"
							not null,
	descuento	num(5,2)	descr "Descuento máximo que puede realizar"
							default 0
							<= 100,
}
primary key (nivel);


table CARPOR 				descr "Cartas de Porte" {
	numcomp		num(8)		descr "Número de comprobante"
							not null,
	trans		num(9)		descr "Número del Transportista"
							not null,
	pendiente 	num(2)		descr "Pendiente"
							in (0 : "Cancelado",
								1 : "Pendiente")
							default 1 not null,
	fecha		date		descr "Fecha"
							not null default today,
	seguro		num(9)		descr "Compañia de Seguro"
							not null,
	zona		char(1)		descr "Codigo de zona"
							not null
							mask "1>x"
							in ZONAS : descrip,
	deposito	num(3)		descr "Depósito del Artículo"
							not null
							in DEPOSIT:(descrip),
	suc			num(3)		descr "Sucursal que origino"
							in SUCURSAL:descrip,
	fecent		date		descr "Fecha de entrega",
	horent		num(2)		descr "Horario de entrega"
							in CODIFICA (12) : descrip,
	grprov		num(2)		descr "Agrupamiento de provincia",
							// in SERV.GRUPROV (2) : descrip
}
primary key (numcomp) [ 3 ],
index FECHA (fecha, numcomp) [ 3 ],
index DEPOSITO (deposito, suc, numcomp) [ 3 ];

table CONFORM	  			descr "Tabla de Conformaciones" {
	numero		num(8)		descr "Número de Conformación"
							not null,
	fecha		date		descr "Fecha de la Confirmacion"
							not null default today,
	trans		num(9)		descr "Código del Transporte",
	feccar		date 		descr "Fecha de la Carta de Porte Asoc."
							default null,
	nrocar		num(8)		descr "Número de Carta de Porte Asoc."
							default null,
	nrohoja		num(8)		descr "Número de Hoja de Ruta."
							default null,
	deposito	num(3)		descr "Depósito del Artículo"
							not null
							in DEPOSIT:(descrip),
	suc			num(3)		descr "Sucursal que origino"
							in SUCURSAL:descrip,
}
primary key (numero) [ 3 ],
index PORTE (nrocar, numero) [ 5 ],
index FECHA (fecha, numero) [ 4 ],
index DEPOSITO (deposito, suc, numero) [ 4 ];


table TIVA					descr "Tabla con los tipos de IVA" {
	tipo		num(2)		descr "Nzmero del tipo de IVA",
//							in (1:"A CONSUMIDOR FINAL",
//								2:"IVA RESPONSABLE INSCRIPTO",
//								3:"IVA RESPONSABLE NO INSCRIPTO",
//								4:"IVA NO RESPONSABLE"),
	descrip		char(30)	descr "Descrip. del tipo de IVA"
							not null mask "30>x",
	porcent		num(5,2)	descr "Porcentaje de IVA",
	descor		char(15)	descr "Descripcion corta"
								not null mask "15>x",

//	Si discrimina -> precio sin iva, sino -> precio con iva.

	discr		bool		descr "Discrimina" not null,

//	El siguiente (si tiene valor) resta porcent.
//	Ej: se carga porcent=27 porcent2=9
//	porcent 27 - 9 = 18
//	porcent2 9

	porcent2	num(5,2)	descr "Porcentaje 2 de IVA"
							not null default 0,
	ventas		bool		descr "Utilizado por Ventas"
							not null default false,
	prov		bool		descr "Utilizado por Proveedores"
							not null default false,
	activo		bool		descr "Activo"
							not null default true,
	cuit		bool		descr "C.U.I.T. obligatorio ?"
							not null default false,
	letra		char		descr "Tipo de Comprobante",
	letracf		char		descr "Letra de Controlador"
	                        in ("I":"Responsable inscripto",
	                        	"N":"Responsable no inscripto",
	                        	"E":"Exento",
	                        	"A":"No responsable",
	                        	"C":"Consumidor final",
	                        	"B":"Resp. no inscripto, venta bienes de uso",
	                        	"M":"Responsable Monotributo",
	                        	"T":"No categorizado"),
	exento      bool       descr "Es Exento"
	                        not null default false,
    tipoimp		num(2)		descr "Tipo de Impresion Habilitada",
}
primary key  (tipo),
index VENTAS (ventas, tipo),
index PROV   (prov, tipo);

table TIMPINT				descr "Tabla de impuestos internos"{
	tipo		num(1) 		descr "Csdigo del impuesto interno." not null,
	descrip		char(20)	descr "Descrip. del impuesto interno" not null,
	porcent		num(5,2)	descr "Porcentaje del impuesto interno.",
	activo		bool		descr "Activo"
							not null default true,
}
primary key (tipo);


table PARSTO				descr "Tabla de Parametros de stock" {
	codigo		num(3)		descr "Código "
							not null,
	descrip		char(30)	descr "Descripcion "
							not null,
	tipo		char(5)		descr "Tipo de Dato"
							not null,
	valor		char(40)	descr "Valor "
							not null,
}
primary key (codigo);


table OPERADOR				descr "Tabla de operadores" {
	codigo		num(2) 		descr "Csdigo de Operador." not null,
	descrip		char(30)	descr "Descrip. del operador" not null,
	activo		bool		descr "Activo"
							not null default true,
	agentid		num(9)		descr "Codigo de Vendedor en EBS", 
}
primary key (codigo);

table MOTCANC				descr "Tabla de motivos de cancelación de Orden de Compra" {
	tipomov		num(2)		descr "Tipo de Movimiento (de Movim)"
								not null,
	numcomp		num(9)		descr "Número de Comprobante"
								not null,
	nroreng		num(4)		descr "Número de Renglón(para los motivos)"
								not null,
	texto		char(75)	descr "Texto Aclaratorio del Motivo",
}
primary key(tipomov, numcomp, nroreng) [7];

table ORDENES 				descr "Detalle de las Ordenes de compra" {
	tipomov		num(2)		descr "Tipo de Orden "
							not null default 14,
	numero		num(8)		descr "Número de Orden"
							not null,
	renglon		num(2)		descr "Renglón de la Orden"
							not null,
	descto		num(5,2)	descr "Porcentaje de descuento",
	recargo		num(5,2)	descr "Porcentaje de recargo",
	iva			num(2)   	descr "Código de Iva",
	impint		num(1)	    descr "Código de Impuesto Interno",
	codadm		char(8)		descr "Código de Administración",
	fecent		date		descr "Fecha de Entrega",
	estado		num(1)		descr "Estado",
	sucur		num(3)		descr "Sucursal",
}
primary key		(tipomov, numero, renglon) [5],
index FECENT (codadm not null, estado not null, sucur not null, fecent desc not null)[5];

table CONDPAG   			descr "Condiciones de Entrega de las OOCC" {

	tipomov		num(2)		descr "Tipo de Orden "
							not null default 14,
	nrocomp		num(9) 		descr "Número de orden de Compra"
							not null,
	nroreng		num(3)		descr "Número de cuota"
							not null,
	tippag      num(1)		descr "Tipo de Pago"
							in (1:"Efectivo",
								2:"Cheque",
								3:"Documento"),
	conpag		num(1)		descr "Condición de Pago"
							in (1:"A Fecha Fija",
								2:"Contra Entrega"),
	dias        num(4)      descr "Días hasta el vencimiento",
//aqui deberia tener el tipomov del informe de recepc 
	nroinf      num(9)      descr "Nro. de informe de recepción, si corresponde",
    fecha       date        descr "Fecha de Vencimiento",
    imporig     num(14,2)   descr "Importe Original en Moneda del Vencimiento",
}
primary key  (tipomov, nrocomp, nroreng) [ 4 ];

table BAJAS					descr "Movimientos dados de baja" {
	tipomov		num(2)		descr "Tipo de movimiento de stock"
							not null
							in TIPOMOV : descrip,
	numcomp		num(8)		descr "Número de comprobante"
							not null,
	codadm		char(8)		descr "Código de artículo de stock movido"
							in CATALOGO : des1adm,
	renglon		num(3)		descr "Número de renglón"
							not null,
	sucursal	num(3)		descr "Sucursal de la Baja"
							in SUCURSAL : descor
							not null,
	fecultmod	date		descr "Fecha de última modificación"
							not null default today,
	cantidad	num(9)		descr "Cantidad",
	cdate		date		descr "Fecha de Carga",
	ctime		time		descr "Hora de Carga",
	cuid		num(5)		descr "Usuario que Cargo",
	mdate		date		descr "Fecha de Modificacion",
	mtime		time		descr "Hora de Modificacion",
	muid		num(5)		descr "Usuario que Modifico",

}
primary key (sucursal, tipomov, numcomp, codadm, renglon) [16], /*Base 300.000 */
index BAJAS (sucursal, fecultmod) [6] ; /* Base 300.000 */


table ZONAS					descr "Zonas de Envio" {

	zona		char(1)		descr "Codigo de zona"
							not null
							mask "1>A",
	descrip		char(20)	descr "Descripcion"
							not null,
	activo		bool		descr "Activo"
							not null default true,
	minflete	num(11,2)	descr "Monto Mínimo de Flete"
							not null
							default 0,
	comflete	num(11,2)	descr "Comisión de Flete"
	 						not null
	 						default 0,
	express		bool		descr "Es Express"
							not null default false,
}
primary key (zona),
unique index activo (activo, zona);

table MOVDTO				descr "Movimientos de Descuento Especial" {

	tipomov		num(2)		descr "Tipo de Movimiento" 
							not null
							in TIPOMOV:descrip,
	numcomp		num(8)		descr "Nro de Comprobante"
							not null,
	dtoini		num(5,2)	descr "Descuento Inicial (Porcentaje)"
							default 0
							not null,
	dtoaut		num(5,2)	descr "Descuento Autorizado (Porcentaje)"
							default 0
							not null,
	dtoreq		num(5,2)	descr "Descuento Requerido (Porcentaje)"
							default 0
							not null,
	valini 		num(11,2)	descr "Descuento Inicial (Valor)"
							not null
							default 0.0,
	valaut		num(11,2)	descr "Descuento Autorizado (Valor)"
							not null
							default 0.0,
	fecha		date		descr "Fecha Movimiento",
	horaaut 	time		descr "Hora Autorizacion",
	suc			num(3)		descr "Sucursal"
							in SUCURSAL : descor
							not null,
    estdto 		num(2)		descr "Estado del Descuento",
	estmov		char(1)		descr "Estado del Movimiento",
	respon		char(1)		descr "Responsable Ultimo"
							mask ">A"
							in ("S":"Solicitante","A":"Autorizante"),
    cantaut    	num(3)		descr "Cantidad de Autorizaciones",
    masinfo		bool  		descr "Requirio mas Informacion",
	codmot		num(2)		descr "Codigo de Motivo",
    emp    		num(3)		descr "Empresa",
    nroleg		num(9)		descr "Nro. de Legajo",
    horamod		time  		descr "Hora. Modificacion Mov. Asociado",
	valreq		num(11,2)	descr "Descuento Requerido (Valor)"
							not null
							default 0.0,
	tipo  		num(2)		descr "Tipo de Registro"
							not null
							default 1,
	auto		bool		descr "Mov. Autorizado",
	flag		num(4)		descr "Flag",
    empfle 		num(3)		descr "Empresa Aut. Flete",
    legfle		num(9)		descr "Nro. de Legajo Aut. Flete",
 	cdate		date		descr "Fecha de Carga",
	ctime		time		descr "Hora de Carga",
	cuid		num(5)		descr "Usuario que Cargo",
	mdate		date		descr "Fecha de Modificacion",
	mtime		time		descr "Hora de Modificacion",
	muid		num(5)		descr "Usuario que Modifico",
}
primary key (tipo, tipomov, numcomp) [14], /* Base 1.000.000 */
index ESTADO (estmov, fecha, horamod) [12],
index FECHA (tipo, fecha) [10];

table VALHSUC				descr "Valores Horarios por Sucursal" {

	tipo		num(3)		descr "Tipo de Valor"
							in TIPVAL : descrip
							not null,
	suc			num(3)		descr "Sucursal"
							in SUCURSAL : descor
							not null,
	fdesde		date		descr "Fecha desde"
							not null,
	hdesde      time		descr "Hora desde"
							not null,
	fhasta		date		descr "Fecha hasta",
	hhasta      time		descr "Hora hasta",
	valor		num(10,2)	descr "Valor"
							not null,
    emp    		num(3)		descr "Empresa",
    nroleg		num(9)		descr "Nro. de Legajo",
	cdate		date		descr "Fecha de Carga",
	ctime		time		descr "Hora de Carga",
	cuid		num(5)		descr "Usuario que Cargo",
	mdate		date		descr "Fecha de Modificacion",
	mtime		time		descr "Hora de Modificacion",
	muid		num(5)		descr "Usuario que Modifico",
}
primary key (tipo, suc, fdesde, hdesde) [4]; /* Base 100.000 */


table RELCOMP				descr "Guarda relación entre los comprobantes de proveedores y los de stock" {
	tipcomp		num(4)		descr "Tipo de Comprobante del proveedor (cnt)"
							not null,
	serie		num(4)		descr "Serie a la que pertenece el comprobante"
							not null,
	nrocomp		num(9)		descr "Nro. comprobante del proveedor"
							not null,
	tipomov 	num(2)		descr "Tipo de movimiento de stock"
							not null,
	numcomp		num(8)		descr "Nro. del movimiento de stock"
							not null,
}
primary key (tipcomp, serie, nrocomp, tipomov, numcomp) [ 6 ],
unique index MOVSTO (tipomov, numcomp, tipcomp, serie, nrocomp) [ 6 ];

table INVENT 				descr "Tabla de inventario" {
	codadm		char(8)		descr "Código de administración"
							in CATALOGO:des1adm,
	deposito	num(3)		descr "Depósito del Artículo"
							in DEPOSIT:(descrip),
	fecha		date		descr "fecha del inventario"
							not null,
	dispvta		num(8)		descr "Disponible Venta",
    exposi	    num(8)	    descr "Exposición",
	pendent		num(8)		descr "Pendiente Entrega",
	service		num(8)		descr "En Service Propio",
	servter		num(8)		descr "En Service de Terceros",
	cargo		num(4)		descr "UsrId",
	feccar		date		descr "Fecha de la Carga" default today,
	horcar		time		descr "Hora de la Carga"
}
primary key	(fecha, deposito, codadm) [ 3 ],
index CODADM (codadm) [ 3 ];

table RELSUBD				descr "Relaciones válidas para transferencias de estados" {
	nivel  		num(2)		descr "Jerarquía del Usuario"
							not null,
	estorig		num(2)		descr "Estado Original de la Mercadería"
							not null in SUBDEP:descrip,
	estdest		num(2)		descr "Estado Destino de la Mercadería"
							not null in SUBDEP:descrip,
	activo		num(1)		descr "Condicion Corriente"
							not null in (1:"Activo",0:"Inactivo") default 0,
	direcc      num(1)      descr "Dirección" 
							not null default 0,
	nrorol		num(2)		descr "Nro. Rol",
	subrol		num(2)		descr "Nro. Sub-Rol",
	reqmot		num(1)		descr "Requiere Motivo ?",
	tipmov		num(2)		descr "Tipo de Motivo",
	nrogrup		num(4)		descr "Número de Grupo",
	artirel		num(2)		descr "Tipo de Relación Asociada",
	reserva		num(2)		descr "Reseva stock?",
	otrasuc     bool		descr "Permite Cambios en Otras Sucursales" 
	            			not null default false,
}
primary key (nivel, estorig, estdest);

table PROVINCIAS			descr "Provincias" {

	prov		char(1)		descr "Código"
							mask ">x"
							not null,
	descrip		char(20)	descr "Nombre"
							mask "20>x"
							not null,
	flag_full	bool		descr "Si llegó al limite máximo de calles x provincia"
							not null
							default false,
	descor 		char(4)     descr "Descrip Corta"
							mask "4>x",
	envcot		num(2)		descr "Envia COT"
							default 0
}
primary key (prov),
unique index	descrip	(descrip);


table NCRED 				descr "Cabecera de Nota de Credito" {

	ncred		num(8)		descr "Nro de N. Credito"
							not null,
	fecha		date		descr "Fecha de N. Credito"
							default today,
	sucursal    num(3)      descr "Sucursal"
							in SUCURSAL : descor
							not null,
	estado		char(1)		descr "Estado de N. Credito"
     						in ("C":"Contabilizada",
								"F":"Facturada",
								"I":"Pendiente de Impresion",
								"Z":"CF - Error")
							default "F"
							mask ">A",
	vendedor	num(4)		descr "Vendedor"
							not null
							in VENDEDORES : vendnom,
	notvta		bool		descr "Existe N. Venta"
							not null default true,
	tipofac		char(1)		descr "Tipo de Factura"
							in ("A":"Fc. Tipo A",
								"B":"Fc. Tipo B",
								"E":"Fc. Tipo E")
							mask ">x",
	seriefac	char(4)		descr "Sucursal Nro."
							mask "4n",
	nrofac		char(8)		descr "Factura Nro."
							mask "8n",
	tipocre		char(1)		descr "Tipo de N. Credito"
							in ("A":"Fc. Tipo A",
								"B":"Fc. Tipo B",
								"E":"Fc. Tipo E")
							mask ">x",
	seriecre	char(4)		descr "Sucursal N. Credito"
							mask "4n",
	nrocre		char(8)		descr "N. Credito Nro."
							mask "8n",
	nombre	     char(30)   descr "Nombre del Cliente."
							not null,
	direccion	 char(30)	descr "Dirección del Cliente."
							not null,
	local		 char(30)	descr "Localidad del Cliente.",
	prov		char(1)		descr "Provincia"
							in PROVINCIAS : descrip
							not null
							mask ">x",
	cuit		char(15)	descr "C.u.i.t. del Cliente.",
	bruto		num(11,2)	descr "Bruto de N. Cred."
							not null
							default 0,
	neto		num(11,2)	descr "Neto de N. Cred."
							not null
							default 0,
	recargo		num(11,2)	descr "Recargo de N. Cred."
							not null
							default 0,
	descuento	num(11,2)	descr "Descuento de N. Cred."
							not null
							default 0,
	comision	num(11,2)	descr "Comision de la N. Cred."
							not null
							default 0,
	tipoiva		 num(2)		descr "Tipo de Iva del Cliente."
							not null
							in TIVA by VENTAS(true) : descrip
				      		default 1,
	fecultmod	 date 		descr "Fecha de última modificación"
							not null
							default today,
	horamod		time		descr "Hora de última modificación"
							not null
							default hour,
	poriva		num(4,2)	descr "Porcentaje de Iva"
							not null
							default 0,
	iva			num(11,2)	descr "Iva de N. Vta."
							not null
							default 0,
	poriva1		num(4,2)	descr "Porcentaje de Iva 1"
							not null
							default 0,
	iva1		num(11,2)	descr "Iva 1 de N. Cred."
							not null
							default 0,
	rg3337		bool		descr "Se le realiza RG3337"
							not null
							default false,
	piva3337	num(4,2)	descr "Porcentaje de Iva RG3337"
							not null
							default 0,
	iva3337		num(11,2)	descr "Iva RG3337"
							not null
							default 0,
	zona		char(1)		descr "Codigo de zona"
							mask "1>x"
	  						in ZONAS : (descrip),
	discr		bool		descr "Discrimina Iva"
							not null default false,
	cpostal		 num(4)		descr "Codigo postal",
	tipdoc		 char(3)	descr "Tipo de Documento.",
	nrodoc		 num (9)	descr "Nro. de Documento.",
	feccot		date		descr "Fecha de Cotizacion",
	horacot		time		descr "Hora de Cotizacion",
	impint		num(11,2)	descr "Impuestos Internos"
							default 0,
	flete		num(11,2)	descr "Flete"
							not null
							default 0,
	dtoesp		num(5,2)	descr "Porcentaje descuento especial"
							default 0
							not null,
    valdtoe		num(11,2)	descr "Descuento especial"
							not null
							default 0,
	flag		num(5)		descr "Flag"
							not null
							default 0,
	nvirel		num(8)		descr "NVI relacionada",						
	cajero		num(5)		descr "Cajero relacionado"						
}
primary key (ncred) [ 7 ],
unique index CREDITO
			(tipocre not null, seriecre not null, nrocre not null, ncred) [ 17 ],
unique index NOTVTA (notvta not null, ncred) [ 7 ],
index FECHA (fecha, ncred) [ 8 ],
index CAJERO (fecha, sucursal, cajero not null, ncred)[12]; /* Base 350.000 */


table SUCURSAL				descr "Sucursales" {

	suc			num(3)		descr "Nro de Sucursal"
							not null,
	descrip		char(30)	descr "Sucursal"
							not null,
	descor		char(10)	descr "Sucursal"
							not null,
	autvta		bool		descr "Habilitada para hacer N.Vta."
							not null default false,
	autcre		bool		descr "Habilitada para hacer N.Cred."
							not null default false,
	//	los siguientes son para testear claves
	uidger1		num(4)		descr "Id del primer gerente.",
	uidger2		num(4)		descr "Id del segundo gerente.",
    ultven      date		descr "Ultimo Asiento de Ventas"
    						not null default today,
	//	el siguiente es para aquellas que no tienen asoc. N.Vta.
	autcrel		bool		descr "Puede hacer N.Cred. libremente"
							not null default false,
	tipsuc		bool		descr "Es Sucursal ?"
							not null default true,
	tipdep		bool		descr "Es Deposito ?"
							not null default false,
	//	si es nulo, pueden ingresar el deposito.
	depcred		num(4)		descr "Depósito para N. Cred.",
	//emp y div representan la division (y empresa) que le corresponde
	//a cada sucursal en aurus. La validacion se hace por programa.
	emp			num(2)		descr "Empresa"
							>= 0,
	div			num(4)		descr "División"
							>= 0,
	modfac		num(1)		descr "Modalidad de Facturación"
							in (1:"Diaria", 2:"Por Lote")
							default 1 not null,
	direc		char(30)	descr "Direccion",
	inicial		char(3)		descr "Iniciales de la sucursal",
	prov		char(1)		descr "Provincia"
							in PROVINCIAS : descrip
							not null
							default "C"
							mask ">x",
	finicio	    date		descr "Fecha de Inicio",						

	direc1		char(40)	descr "Direccion 1",
	direc2		char(40)	descr "Direccion 1",
	direc3		char(40)	descr "Direccion 1",
	direc4		char(40)	descr "Direccion 1",
	factman		bool		descr "Facturación Manual"
							default false,
	finiauto	date		descr "Fecha de Inicio de Fact. Automática",
	estab		char(20)	descr "Nro Establecimiento",
	sede		char(3)		descr "Sede Timbrado",
	codlocal	num(5)		descr "Código de Localidad",
							// in gral.LOCALPROV:descrip
	ultstkpr  	date		descr "Ultimo Asiento Stock del proveedor",
	fultgenb	date		descr "Ultima generación Comprobantes B promos", 
	estcie		num(2)		descr "Ultimo estado del cierre de sucursal",
	cpostal		num(4)		descr "Codigo postal",
	fcierre		date		descr "Fecha de Cierre de una Sucursal",
	tiposuc		num(3)		descr "Tipo de Sucursal"
							in CODIFICA(358), 	
	letrasuc	char(4)		descr "Letra identificatoria de Sucursal"
							in CODIFC(24),
}
primary key (suc),
unique index TIPSUC (tipsuc, suc),
unique index TIPDEP (tipdep, suc),
unique index EMP (emp not null, div not null),
unique index MOD (modfac, suc);


table RELZONA				descr "Relacion entre zonas y partidos/comisarias" {

	codigo		char(2)		descr "Codigo"
							not null,
	descrip		char(20)	descr "Descripcion"
							not null,
	zona		char(1)		descr "Zona asociada"
							not null
							mask ">A"
							in ZONAS : (descrip),
}
primary key (codigo),
index ZONA  (zona, codigo);

table REIMP 				descr "Comprobantes Reimpresos" {

	tipomov 	num(2)		descr "Tipo de movimiento de Stock"
							in TIPOMOV : descrip
							not null,
	numcomp		num(8)		descr "Numero interno"
							not null,
	tipo		char(1)		descr "Tipo"
							not null,
	serie		char(4)		descr "Serie"
							not null,
	numero		char(8)		descr "Numero"
							not null,
	fecha		date		descr "Fecha"
							not null,
	hora		time		descr "Hora"
							default hour
							not null,
	sucursal	num(3)		descr "Sucursal"
							in SUCURSAL : descor
							not null,
	cajero		num(5)		descr "Cajero",
	renglon		num(2)		descr "Renglon"
							not null
							default 0,
	cdate		date		descr "Fecha de Carga",
	ctime		time		descr "Hora de Carga",
	cuid		num(5)		descr "Usuario que Cargo",
	mdate		date		descr "Fecha de Modificacion",
	mtime		time		descr "Hora de Modificacion",
	muid		num(5)		descr "Usuario que Modifico",

}
primary key (tipomov, numcomp, renglon)[5];

table TIPCONF				descr "Tipo de Confirme" {

	tipo		char		descr "Tipo"
							not null
							mask ">A",
	descrip		char(15)	descr "Descripción"
							not null,
	descor		char(5)		descr "Descripción corta"
							not null,
	valoriza    bool	    descr "Se incluye en Valorizacion de Fletes(S/N)"
							not null
							default true,						
	rerutea		bool	    descr "Permite re-reutear(S/N)"
							not null
							default false,						
	ctrtrans	num(2)		descr "Control transportista para el rerruteo"
							in CODIFICA(273)
							not null
							default 0,
	ctrcot		bool		descr "Controla envio a COT?"
							not null
							default true,
	ctrlsal		bool		descr "Controla confirmacion salida HR"
							not null
							default true,
	esfall		bool		descr "Es valido para fallido"
							not null
							default false,

}
primary key (tipo),
unique index fallido (esfall, tipo);


table HRUTA					descr "Hoja de Ruta" {

	nrohoja		num(8)		descr "Hoja de Ruta"
							not null,
	fecha		date		descr "Fecha"
							not null default today,
	trans		num(9)		descr "Número del Transportista"
							not null,
	seguro		num(9)		descr "Compañia de Seguro"
							not null,
	confirmada 	bool		descr "Confirmada"
							default false not null,
	deposito	num(3)		descr "Depósito del Artículo"
							not null
							in DEPOSIT:(descrip),
	suc			num(3)		descr "Sucursal que la origino"
							in SUCURSAL:(descrip),
	fletero		char(20)	descr "Fletero",
	estado		char(1)		descr "Estado de la Hoja de Ruta"
							in ("P":"Provisoria",
								"C":"Cerrada",
								"D":"Definitiva"),
	jaula		char(6)		descr "Nro. de Jaula",
	comfle		num(2)		descr "Medio de Comunicacion del Fletero"
							in CODIFICA(107):descrip,
	ncomfle     char(15)    descr "Numero de Medio de Comunicacion del Fletero",							
	mentrega	char(1)		descr "Modo de entrega"
							mask ">A"
							in ("D":"Domiciliarias",
								"E":"Express",
								"T":"Venta telefonica interior",
							    "N":"No valorizada"),
    cantimp		num(1)		descr "Cantidad de Impresiones",							    
	acompan		char(20)	descr "Acompaniante del Fletero",
	patente		char(8)		descr "Patente del Flete",
	despacho	char(20)	descr "Despacho",
    cond		num(2)		descr "Condiciones",
	fconfi		date		descr "Fecha de confirmacion de datos.",
	hconfi		time		descr "Hora de confirmacion de datos.",
	uconfi		num(5)		descr "Usuario que confirmo los datos.",
	fecdes		date		descr "Fecha de despacho.",
	ctrl		char(20)	descr "Persona que carga el camión",
    sucdest		num(3)		descr "Sucursal de destino",
}
primary key (nrohoja) [ 3 ],
unique index CONF (deposito, confirmada, nrohoja) [ 4 ],
unique index FECHA (fecha, nrohoja) [ 4 ],
index DEPOSITO (deposito, suc, nrohoja) [ 4 ],
index FCONFI (fconfi, nrohoja) [ 4 ];


table MRUTA					descr "Movimientos de Hoja de Ruta" {

	nrohoja		num(8)		descr "Hoja de Ruta"
							not null
							in HRUTA : fecha,
	remito		num(8)		descr "Remito Interno"
							not null,
	tipomov		num(2)		descr "Tipo de Comprobante (Refer.)"
							not null
							in TIPOMOV : reduc,
	numcomp		num(8)		descr "Numero de Comprobante (Refer.)"
							not null,
	carpor		num(8)		descr "Número de Carta de Porte"
							not null,
	nroconf		num(8)		descr "Numero de Confirme"
							default null,
	est			char		descr "Estado"
							default "S"
							mask ">A"
							not null
							in TIPCONF : descrip,
	estref		char		descr "Estado del Comp. de Referencia",
	renglon		num(2) 		descr "Nro. Renglón",
	suc			num(3)		descr "Sucursal que la origino"
							in SUCURSAL:(descrip),
}
primary key (nrohoja, remito) [8],
unique index REMITO (remito, nrohoja) [8],
unique index ESTADO (est, remito, nrohoja) [9],
index        RENGLON (nrohoja, renglon) [6],
index SUCURSAL(nrohoja,tipomov,suc) [7];   

table PLANILLA 				descr "Planilla de recepción entre Sucursales" { 
	sucursal	num(3)		descr "Sucursal"
							in SUCURSAL : descor
							not null,
	fecha		date		descr "Fecha"
							not null,
	nrocomp		num(8)		descr "Numero interno"
							not null,
	codadm		char(8)		descr "Código de administración"
							in CATALOGO:(des1adm)
							not null
							mask $codadm,
    cant        num(8)      descr "Cantidad"
							not null,
   	cumplido    num(1)      descr "Cumplimentado"
							not null default 0,
	deposito    num(3)		descr "Depósito Asociado"
							in SUCURSAL not null default 1,
}
primary key (sucursal, fecha, nrocomp, codadm) [17],   /* Base 350.000 */
index ESTADO (sucursal, cumplido, codadm, fecha, nrocomp) [18];

table DISTRIB 				descr "Planilla de Distribución entre Sucursales" { 
	fecha		date		descr "Fecha"
							not null,
	codadm		char(8)		descr "Código de administración"
							in CATALOGO:(des1adm)
							not null
							mask $codadm,
	sucursal	num(3)		descr "Sucursal"
							in SUCURSAL : descor
							not null,
    cant        num(4)      descr "Cantidad"
							not null,
	estado      num(1)      descr "Estado"
							not null default 0,
	deposito    num(3)      descr "Depósito Asociado"
							in SUCURSAL not null default 1,
}
primary key (fecha, codadm, sucursal) [ 8 ];

table CONDIAS   			descr "Condiciones de Pago de las OOCC" {

	tipomov		num(2)		descr "Tipo de Orden "
							not null default 14,
	nrocomp		num(9) 		descr "Número de orden de Compra"
							not null,
	nroreng		num(3)		descr "Número de cuota"
							not null,
	dias        num(4)      descr "Días hasta el vencimiento",
    fecha       date        descr "Fecha fija al Vencimiento",
}
primary key  (tipomov, nrocomp, nroreng) [ 4 ];


table CMPSUC				descr "Condiciones Medios de Pagos x Suc." {

	suc			num(3)		descr "Sucursal"
							in SUCURSAL : descor
							not null,
	codigo		num(2)		descr "Código del Medio de Pago"
							not null
							in MEDIOSP : descrip,
	subcod		num(3)		descr "Desglose de los M. Pagos"
							not null
							in SUBMEDIOS(codigo) : descrip,
	cuotas		num(2)		descr "Cantidad de cuotas - pagos"
							not null
					in CONDMP (codigo, subcod) : (descto, tasa, activo),
	fechad		date		descr "Fecha de vigencia (desde)"
												// inclusive
							not null,
	fechah		date		descr "Fecha de vigencia (hasta)"
												// inclusive
							not null,
	descto		num(4,2)	descr "Descuento (%)"
							not null,
	tasa		num(4,2)	descr "T.I.M.D. (%)" // % int. mens. direct.
							not null,
}
primary key (suc, codigo, subcod, cuotas, fechad),
index FECHA (suc, fechah, fechad);

table TIPVAL				descr "Tipo de Valores" {

	codigo		num(3)		descr "Código"
							not null,
	descrip		char(30)	descr "Descripción"
							not null,
	descor		char(10)	descr "Descripción corta"
							not null,
	fhasta		bool		descr "Utiliza Fecha Hasta"
							not null
							default false,
	hora  		bool		descr "Utiliza Horas"
							not null
							default false,
	coniva		bool		descr "Incluye I.V.A."
							default false
							not null,
	xsuc		bool		descr "Desglose por Sucursal"
							default false
							not null,
	tipo		char(1)		descr "Tipo de Valor"
							in ("P" : "Porcentaje",
								"C" : "Coeficiente",
								"V" : "Valor")
							mask ">A"
							default "V"
							not null,
	dspmsg		bool		descr "Mensaje en grabaciones internas mal"
							not null
							default true,
	fijafh		bool		descr "Fija Fecha Hasta"
							not null
							default false,
	rol			num(2)		descr "Rol o Función",
	subrol		num(2)		descr "Subrol o Función",
	codmot		num(4)		descr "Cod. Serie Motivo u Observacion",
	ctrlporc	num(3)		descr "Controla Porcentaje segun tipo"
							not null default 0,
	ctrlval 	num(3)		descr "Controla Valor segun tipo"
							not null default 0,
	flagtip 	num(3)		descr "Flag por tipo"
							not null default 0,
	fhoy		bool		descr "Permite F.Hoy"
							not null
							default false,
	sucadm		bool		descr "Admite Sucursal Administracion Central"
							not null
							default false,
	solodiv  	bool		descr "Solo permite la sucursal de la division"
							not null
							default false,
}
primary key (codigo);

table VALART				descr "Valores por articulo" {

	tipo		num(3)		descr "Tipo de Valor"
							in TIPVAL : descrip
							not null,
	suc			num(3)		descr "Sucursal"
							in SUCURSAL : descor
							not null,
	codadm		char(8)		descr "Código de administración"
							not null
							in CATALOGO : des1adm,
	fdesde		date		descr "Fecha desde"
							not null,
	hdesde      time		descr "Hora desde"
							not null,
	fhasta		date		descr "Fecha hasta",
	hhasta      time		descr "Hora hasta",
	valor		num(10,2)	descr "Valor"
							not null,
	cdate		date		descr "Fecha de Carga",
	ctime		time		descr "Hora de Carga",
	cuid		num(5)		descr "Usuario que Cargo",
}
primary key (tipo, suc, codadm, fdesde, hdesde) [14],  /*Base 300.000 */
index FECHA (tipo, suc, fdesde, hdesde, codadm) [14];

table VALFAM				descr "Valores por familia" {

	tipo		num(3)		descr "Tipo de Valor"
							in TIPVAL : descrip
							not null,
	suc			num(3)		descr "Sucursal"
							in SUCURSAL : descor
							not null,
	codflia		char(8)		descr "Código de familia"
							in FAMILIA : des1flia
							not null
							mask $codflia,
	fdesde		date		descr "Fecha desde"
							not null,
	fhasta		date		descr "Fecha hasta",
	valor		num(10,2)	descr "Valor"
							not null,
}
primary key (tipo, suc, codflia, fdesde) [9];


table VALMAR				descr "Valores por marca" {

	tipo		num(3)		descr "Tipo de Valor"
							in TIPVAL : descrip
							not null,
	suc			num(3)		descr "Sucursal"
							in SUCURSAL : descor
							not null,
	marca		num(8)		descr "Código de marca"
							in MARCA : descrip
							not null,
	fdesde		date		descr "Fecha desde"
							not null,
	fhasta		date		descr "Fecha hasta",
	valor		num(10,2)	descr "Valor"
							not null,

}
primary key (tipo, suc, marca, fdesde);

table LISTREG				descr "Lista de Regalos de Casamiento" {

	nrolista	num(6)		descr "Número de Lista de Casamiento"
							not null,
	conyuges	char(30)	descr "Apellidos y Nombres de los Conyuges"
							not null mask "30>x",
	direccion	char(30)	descr "Dirección de Envío"
							not null mask "30>x",
	local		char(30)	descr "Localidad"
							mask "30>x" not null,
	prov		char(1)		descr "Provincia"
							in PROVINCIAS : descrip
							not null
							mask ">x",
	cpostal		num(4)		descr "Codigo postal",
	area		char(5)		descr "Numero de DDN",
	telefono	char(8)		descr "Numero de telefono",
	zona		char(1)		descr "Codigo de zona"
							mask "1>x"
							in ZONAS : (descrip),
	fenlace    	date		descr "Fecha de Enlace"
							not null,
	fentrega	date		descr "Fecha de Entrega",
	vendedor	num(4)		descr "Vendedor que realiza la operación"
							in VENDEDORES : vendnom not null,
	estado		char(1)		descr "Estado"
							in ("A":"Activa",
								"I":"Inactiva",
								"L":"Lockeado/Bloqueado",
								"C":"Cerrada",
								"T":"Total")
							mask ">A" default "A",
    falta       date        descr "Fecha de Alta",
    sucur       num(3)      descr "Sucursal"
    						in SUCURSAL:descrip,
	horent		num(2)		descr "Horario de entrega"
							in CODIFICA (12) : descrip,
	repetir		bool		descr "Posib. de repetir los productos",
	agregar		bool		descr "Posib. de agregar prod. fuera de lista",
	reempl		bool		descr "Reempl. por prod. similares (si no hay stock)",
	novio		char(30)	descr "Apellidos y Nombres del Novio"
							mask "30>x",
	novia		char(30)	descr "Apellidos y Nombres de la Novia"
							mask "30>x",
	codlocal	num(5)		descr "Código de Localidad",
							// in gral.LOCALPROV:descrip
	codnovio    num(8)		descr "Codigo de Novio",
							// in serv.CTADAT:apynom
	codnovia    num(8)		descr "Codigo de Novia",
							// in serv.CTADAT:apynom
	factiva	    date		descr "Fecha de reactivacion despues de haber estado bloqueada",
	saldo		num(11,2)	descr "Saldo a favor luego de los canjes"
							not null default 0.0,
	nrosec		num(2)		descr "Número de Secuencia de Canjes"
							not null default 0,
	empleg		num(2) 		descr "Empresa del Legajo",
	nroleg		num(9)		descr "Nro. Legajo del Oficial de Cuentas",
	cctral		bool		descr "Trabaja con Cierre Centralizado"
							not null
							default false,
	saldoini	num(11,2)	descr "Saldo Inicial"
							not null 
							default 0.0,
	saldoinc	num(11,2) 	descr "Saldo a Incrementar"
							not null 
							default 0.0,
	impdev		num(11,2) 	descr "Importe a Devolver"
							not null
							default 0.0,
    totcred		num(11,2) 	descr "Total Credito de la Lista"
							not null
							default 0.0,
	pjeinc		num(2) 		descr "Pje. de Incremento de la lista",
	pjedev		num(2) 		descr "Pje. de Devolucion",
	feccie		date		descr "Fecha de Cierre de la Lista",
	fecdev		date		descr "Fecha en que se hizo la devolucion",
	depura		bool		descr "Depura ?",
	devcash		bool		descr "Devolvio efectivo ?",
	sucdev		num(3)		descr "Sucursal donde se realizo devolucion efectivo",
	cajdev		num(5)		descr "Cajero que realizo la devolucion",
	nrocomp     num(9)		descr "Nro. Cbte. de Mov. de Caja",
	saldev		num(11,2) 	descr "Saldo a Devolver"
							not null
							default 0.0,
	fecasi    	date		descr "Fecha de asignacion de la devolucion",
	impdto      num(11,2)   descr "importe beneficio 5% del totcred"
							not null
							default 0.0,
	saldto      num(11,2)   descr "saldo beneficio 5% del totcred" 
	                        not null
	                        default 0.0,
	tiplist		num(2)		descr "Tipo de listas",
	stiplist  	num(2)		descr "Subtipo listas",
	accmkt		char(4)		descr "Accion Marketing"
							in CODIFC(25) : descrip,
}
primary key (nrolista) [2],  /* Base 20.000 */
index NOMBRE (conyuges, nrolista) [8],
index ACTIVA (estado, nrolista) [2],
index VENDEDOR (vendedor, nrolista) [2],
index FECHA (falta not null, vendedor not null) [2],
index TIPLIST (tiplist, stiplist, nrolista) [2];

table ARTIREG				descr "Artículos por Familia que conforman la lista" {

	nrolista	num(6)		descr "Número de Lista de Casamiento"
							not null in LISTREG:conyuges,
	codadm		char(8)		descr "Código de administración"
							in CATALOGO:(des1adm)
							not null
							mask $codadm,
    renglon     num(2)		descr "Número de Artículo dentro de la lista para la familia"
							not null,
	cantidad	num(6)		descr "Cantidad Pedida"
							not null default 0,
	codflia		char(8)		descr "Código de Familia"
							in FAMILIA:(des1flia)
							not null
							mask $codflia,
	tipo		char(1)		descr "Tipo de Articulo"
							in ("O":"Original","A":"Agregado") mask ">A" default "O",
    estado		char(1)     descr "Estado del Artículo",

} primary key (nrolista, codadm) [56],  /* Separ max, el recomedado es mayor */
unique index FAMILY (nrolista, codflia, codadm) [56], 
unique index ORDEN  (nrolista, codflia, renglon) [56];

table COMPREG				descr "Comprobantes asociados a la lista" {

	nrolista	num(6)		descr "Número de Lista de Casamiento"
							not null in LISTREG:conyuges,
	codadm		char(8)		descr "Código de administración"
							in CATALOGO:(des1adm)
							not null
							mask $codadm,
	tipomov 	num(2)		descr "Tipo de movimiento de Stock"
							in TIPOMOV
							not null,
	numcomp		num(8)		descr "Número de nota de venta/cred"
							not null,
	cantidad    num(6)		descr "Cantidad"
							not null default 0,
	nrocanje	num(2)		descr "Número de Canje Relacionado",
	precio		num(11,2)	descr "Precio Contado",
	escanjei	bool		descr "Es canje Inicial?"
							not null
							default FALSE,	
}
primary key (nrolista, codadm, tipomov, numcomp) [15],  /*Base 250.000 */
unique index COMPROB (nrolista, tipomov, numcomp, codadm) [15]; 

table FAMIREG				descr "Familias que conforman la lista" {

	nrolista	num(6)		descr "Número de Lista de Casamiento"
							not null in LISTREG:conyuges,
	codflia		char(8)		descr "Código de Familia"
							in FAMILIA:(des1flia)
							not null
							mask $codflia,
    renglon     num(2)		descr "Número de Familia dentro de la lista"
							not null,
	cantidad	num(6)		descr "Cantidad Pedida"
							not null,
    prioridad   num(2)		descr "Prioridad de Selección",
	cante       num(6)		descr "Cantidad Facturada"
							default 0 not null,
	tipo		char(1)		descr "Tipo de Familia"
							in ("O":"Original","A":"Agregada") mask ">A" default "O",

}
primary key (nrolista, codflia) [56],   /* Base 200.000 */
unique index ORDEN  (nrolista, renglon) [40];


table VALSUC				descr "Valores por sucursal" {

	tipo		num(3)		descr "Tipo de Valor"
							in TIPVAL : descrip
							not null,
	suc			num(3)		descr "Sucursal"
							in SUCURSAL : descor
							not null,
	fdesde		date		descr "Fecha desde"
							not null,
	fhasta		date		descr "Fecha hasta",
	valor		num(10,2)	descr "Valor"
							not null,
}
primary key (tipo, suc, fdesde);


table OPERTIPO				descr "Tipo de Operaciones (N.Vta.)" {

	tipo		num(2)		descr "Tipo"
							not null,
	descor		char(10)	descr "Descripcion corta"
							not null,
}
primary key (tipo);


table OPERNVTA				descr "Operaciones (N.Vta.)" {

	oper		char(2)		descr "Operacion"
//							mask ">A"
							not null,
	descrip		char(15)	descr "Descripcion"
							not null,
	tipo		num(2)		descr "Tipo de Operacion"
							in OPERTIPO : descor
							not null,
	ingresa		bool		descr "Puede ingresar manualmente"
							not null,
	deposito	num(3)		descr "Deposito (para C.Porte)"
							in DEPOSIT : descort,
	vtaexp 		bool		descr "Venta Express",
}
primary key (oper),
unique index INGRESA (ingresa, oper);


table OPERSUC				descr "Oper. por Sucursal (N.Vta.)" {

	suc			num(3)		descr "Sucursal"
							in SUCURSAL by TIPSUC (true) : descor
							not null,
	oper		char(2)		descr "Operacion"
//							mask ">A"
							in OPERNVTA by INGRESA (true) : descrip
							not null,
	origen		num(3)		descr "Suc-Dep Origen de la Oper."
							in SUCURSAL : descor
							not null,
	activo		bool		descr "Activo"
							not null default true,
}
primary key (suc, oper),
unique index ACTIVO (activo, suc, oper);


table OPERORD				descr "Orden de las Oper. (N.Vta.)" {

	suc			num(3)		descr "Sucursal"
							in SUCURSAL by TIPSUC (true) : descor
							not null,
	oper		char(2)		descr "Operacion"
//							mask ">A"
							in OPERSUC (suc) : origen
							not null,
	orden		num(2)		descr "Orden de Busqueda"
							>= 0
							not null,
	codorig		num(3)		descr "Deposito Origen"
							in DEPOSIT : descort
							not null,
	scodorig	num(2)		descr "Estado Origen"
							in OPERORIG (codorig) : activo
							not null,
	coddest		num(3)		descr "Deposito Destino"
							in DEPOSIT : descort,
	scoddest	num(2)		descr "Estado Destino"
							in SUBDEP : descort,
	newoper		char(2)		descr "Nueva Oper."
//							mask ">A"
							in OPERNVTA : descrip
							not null,
	estmovn		num(2)		descr "Estado del MOVIM (N. Vta.)"
							not null,
	estmovf		num(2)		descr "Estado del MOVIM (Facturacion)",
	priori		num(1)		descr "Prioridad"
							>= 0
							default 0
							not null,
	fmovdeb		char		descr "Flag para el MOVDEB"
							mask ">X"
							//	Valores reservados
							not in ("V":"V",
									"W":"W",
									"X":"X",
									"Y":"Y",
									"Z":"Z")
							not null,
	activo		bool		descr "Activo"
							not null default true,
	confstk		bool		descr "Confirma reserva de stock"
							not null default false,
	stkajeno	bool		descr "Permitido para sacar de otros depositos"
							not null default false,
	stkprove	bool		descr "Venta Realizada con stock del proveedor"
							not null default false,
}
primary key (suc, oper, orden),
unique index ORIGEN (suc, oper, codorig, scodorig),
unique index MOVDEB (suc, oper, fmovdeb),
index NEWOPER (suc, newoper, codorig, scodorig);


table OPERCVAL				descr "Cambios validos de Oper.(N.Vta.)" {

	suc			num(3)		descr "Sucursal"
							in SUCURSAL by TIPSUC (true) : descor
							not null,
	oper		char(2)		descr "Operacion"
//							mask ">A"
							in OPERNVTA : descrip
							not null,
	newoper		char(2)		descr "Nueva Operacion"
//							mask ">A"
							in OPERSUC (suc) : origen
							not null,
	ncodorig	num(3)		descr "Nuevo Deposito Origen"
							in DEPOSIT : descort
							not null,
	nscodorig	num(2)		descr "Nuevo Estado Origen"
							in OPERORD by ORIGEN (suc,newoper,ncodorig):
									(newoper,coddest,scoddest)
							not null,
	fmovdeb		char		descr "Flag para el MOVDEB"
							mask "<A"
							//	Valores reservados
							not in ("V":"V",
									"W":"W",
									"X":"X",
									"Y":"Y",
									"Z":"Z")
							not null,
	activo		bool		descr "Activo"
							default true not null,
}
primary key (suc, oper, newoper),
unique index MOVDEB (suc, oper, fmovdeb);


table OPERORIG				descr "Origenes. de las Oper. (N.Vta.)" {

	codorig		num(3)		descr "Deposito Origen"
							in DEPOSIT : descort
							not null,
	scodorig	num(2)		descr "Estado Origen"
							in SUBDEP : descort
							not null,
	activo		bool		descr "Activo"
							default true not null,
}
primary key (codorig, scodorig);


table OPEROBS				descr "Observ. de los Orig. (N.Vta.)" {

	codorig		num(3)		descr "Deposito Origen"
							in DEPOSIT : descort
							not null,
	scodorig	num(2)		descr "Estado Origen"
							in OPERORIG (codorig) : activo
							not null,
	tipo		char(1)		descr "Tipo de Obs."
							in ("E" : "Existe",
								"N"	: "No Existe")
							not null,
	renglon		num(2)		descr "Renglon de la Obs."
							>= 0
							not null,
	descrip		char(40)	descr "Observacion"
							not null,
}
primary key (codorig, scodorig, tipo, renglon);

table VALCOMP  				descr "Valores adicionales de comprobantes"  {

	tipomov 	num(2)		descr "Tipo de movimiento"
							in TIPOMOV : reduc
							not null,
	numcomp		num(8)		descr "Número de comprobante"
							not null,
	tipoval		num(3)		descr "Tipo de Valor"
							in TIPVAL : descrip
							not null,
	valor		num(11,2)	descr "Valor"
							default 0
							not null,
}
primary key (tipomov, numcomp, tipoval) [55],     /* Base 13.000.000 */
unique index TIPOVAL (tipoval, tipomov, numcomp) [55];

table MPTASAS				descr "Tasas por Medio de Pago" {

	tipo   		num(2)		descr "Tipo de Valor"
							not null
							in CODIFICA (49) : descrip,
	codigo		num(2)		descr "Código del Medio de Pago"
							not null
							in MEDIOSP : descrip,
	subcod		num(3)		descr "Desglose de los M. Pagos"
							not null
							in SUBMEDIOS(codigo) : descrip,
	cuotas		num(2)		descr "Cantidad de cuotas - pagos"
							in CONDMP (codigo, subcod) : (tasa, activo),
	fechad		date		descr "Fecha de vigencia (desde)"
							not null,
	fechah		date		descr "Fecha de vigencia (Hasta)",
	valor		num(4,2)	descr "Valor"
							not null,
}
primary key (tipo,codigo, subcod, cuotas, fechad) [ 2 ];

table VALVEND				descr "Porcentajes de Comisiones por Vendedor" {

	vendnum		num (4)		descr "Numero del Vendedor"
						 	      not null
                                  in VENDEDORES:vendnom,
	fechad		date		descr "Fecha de vigencia (desde)"
							not null,
	fechah		date		descr "Fecha de vigencia (Hasta)",
    vendcom     num (5,3)   descr "Comision del Vendedor por Contado"
				    			  not null
                                  default 0.0
                                  between 0 and 99.999 ,
	vendcre		num (5,3)   descr "Comision del Vendedor por Credito"
                                  not null
                                  default 0.0
                                  between 0 and 99.999,
}
primary key (vendnum, fechad);

table COMVGAR				descr "Comisiones de vendedor para Gar. Ext." {

	codigo		num(2)		descr "Código de Comision"
							not null,
	descrip		char(30)	descr "Descripción"
							not null,
	descor		char(10)	descr "Descripción Corta"
							not null,
	comctdo		num(5,2)	descr "Comision Contado (%)"
							not null,
	comcred		num(5,2)	descr "Comision Credito (%)"
							not null,
	comcper		num(5,2)	descr "Comision Creditos Personales (%)"
							not null,
	rango		num(2)		descr "Rango de porcentaje"
							in CODIFICA (16) : descrip
							not null > 0,
}
primary key (codigo);

table RCOMVGAR				descr "Rango para comis. de vend. para Gar. Ext." {

	tipcod		num(3)		descr "Tipo de Código"
//							in TIPCODIF : descrip
							not null,
	codigo		num(2)		descr "Codigo de Rango"
							in CODIFICA (tipcod) : descrip
							not null > 0,
	base		float		descr "Base (%)"
							not null,
	porc		num(5,2)	descr "Porc. a aplicar (%)"
							not null,
}
primary key (tipcod, codigo, base);

table TMCOM					descr "Relacion movimientos con cbtes de AURUS" {
	tipomov		num(2)		descr "Tipo de movimiento de compras"
							in TIPOMOV:descrip
							not null,
	emp			num(2)		descr "Empresa"
							not null,
	trans		num(2)		descr "Transaccion por la que se carga"
							not null
							in CODIFICA (5) : descrip,
	activo		bool		descr "La relacion esta activa?"
							not null
							default true,
	movrel		num(2)		descr "Movimiento relacionado",
	actcos		bool		descr "Indica si Actualiza el costo de los pdtos.?"
							not null
							default true,
}
primary key (tipomov),
index EMPTR (emp, trans, tipomov),
index MOVREL (emp, trans, movrel not null, tipomov);

table IMPCCOM				descr "Conc/Impc para los movims x cbte de AURUS" {
	tipcomp		num(4)		descr "Tipo de cbte de AURUS"
							not null,
	aper		num(3)		descr "Apertura de comprobantes"
							not null
							in CODIFICA (6) : descrip,
	tipcot		num(2)		descr "Tipo de Cotizacion",
	concepto	num(2)		descr "Concepto de AURUS"
							not null,
	nroimp		num(2)		descr "Imputacion de AURUS"
							not null,
	activo		bool		descr "La relacion esta activa?"
							not null
							default true,
	divi		num(3)		descr "La Division",
	depto		num(4)		descr "El Departamento",
	renglon		num(4)		descr "Renglón"
							not null default 0,
	xdef     	bool        descr "Es Valor por Default"
							not null default true,
    tipcompi	num(4)		descr "Comprobante Relacionado a Aplicar",
    nrocta		num(6)		descr "Nro. de Cta Cble para interfaz con EBS",
    signo		num(1)		descr "Signo de la transaccion a generar"
    							in ( 1: "Debito",
    								-1: "Credito"),
	descrip		char(20)	descr "Descripción",
	uso			num(2)		descr "Uso de la Imputación",
	tipcctei	num(2)		descr "Tipo de Cuenta Corriente a Imputar",
	operfin		char(1)		descr "Operación Financiera"
								in ("I":"Inicia Vencimiento", 
									"A":"Aplica a Vencimiento Existente"),
}
primary key (tipcomp, aper, tipcot, renglon),
unique index XDEF (tipcomp, xdef, aper, tipcot, tipcompi, renglon),
unique index CONIMP (tipcomp, concepto, nroimp, tipcot, renglon);

table TCMOV					descr "Rel. de cbtes de AURUS c/movims de COMPRAS" {
	tipcomp		num(4)		descr "Tipo de cbte de AURUS"
								not null,
	tipomov		num(2)		descr "Tipo de movimiento de compras"
							in TIPOMOV:descrip,
	emp			num(2)		descr "Empresa"
							not null,
	trans		num(2)		descr "Transaccion por la que se carga"
							not null
							in CODIFICA (5) : descrip,
	activo		bool		descr "La relacion esta activa?"
							not null
							default true,
	smovsto		bool		descr "Indica si se llaman sin mov. de Stock ?"
							not null
							default true,
	tipcomp_c	num(4)		descr "Tipo de Comprobante de Aurus que lo cancela"
							in tcmov by emptr(emp, 7):activo,
    tipoper     num(2)      descr "Tipo de Operación"
							in CODIFICA(7) : descrip,
    tctrl		char(1)		descr "Tipo Cbte. p/Controlador", 
	tccte		num(2)		descr "Tipo de Cta. Cte. AURUS",
	depto		num(4)		descr "Departamento Relacionado",
	gruexcl		num(4)		descr "Grupo de exclusion",
	enviapdf	bool		descr "Envia mail con adjunto Pdf a Proveedores?"
							not null
							default false,
	turno		bool		descr "Es un Comprobante a Turnar?"
							not null
							default false,
	grabaq		bool		descr "Indica si graba en COLA para EBS"
							not null
							default false,
	aurus		bool		descr "Indica si graba en AURUS"
							not null
							default true,
}
primary key (tipcomp),
index EMPTR (emp, trans, tipcomp),
index EMPTRS (emp, smovsto, tipcomp),
unique index TIPOMOV (tipomov not null),
index TOPER (tipoper not null, tipcomp);

table DETALLES				descr "Coment/observaciones de comprobantes" {
	emp			num(2)		descr "Empresa de Aurus"
							not null,
	tipcomp		num(4)		descr "T.C. de Aurus"
							not null,
	serie		num(4)		descr "S.C. de Aurus"
							not null,
	nrocomp		num(9)		descr "N.C. de Aurus"
							not null,
	reng		num(2)		descr "Renglon del comentario"
							not null,
	detalle		char(60)	descr "Detalle"
							not null,
}
primary key (emp,tipcomp,serie,nrocomp,reng) [ 7 ];

table SENIA 				descr "Cabecera de Factura de Seña" {
	numcomp		num(8)		descr "Número de seña"
							not null,
	fecha		date		descr "Fecha"
							default today,
	estado		char(1)		descr "Estado"
      						in ("P":"Pendiente",
								"A":"Autorizada",
								"X":"Vencida Contabilizada",
	     						"C":"Cancelada",
								"I":"Pendiente de Impresion",
								"Z":"CF - Error",
								"N":"Anulada",
								"B":"NSI NoImpresa",
								"V":"Vencida",
	  							"E":"Pendiente de Cancelar") 
	  						default "P"
							mask ">A",
	sucursal    num(3)      descr "Sucursal donde se realizo la venta"
							not null in SUCURSAL:descor,
	tipofac		char(1)		descr "Tipo de Factura"
							in ("A":"Fc. Tipo A",
								"B":"Fc. Tipo B",
								"E":"Fc. Tipo E",
								"X":"Recibo X",
								"Z":"Recibo B")
							mask ">x",
	seriefac	char(4)		descr "Sucursal Nro."
							mask "4n",
	nrofac		char(8)		descr "Factura Nro."
							mask "8n",
	neto		num(11,2)	descr "Neto"
							not null,
	nombre	     char(30)   descr "Nombre del Cliente."
							not null,
	direccion	 char(30)	descr "Dirección del Cliente."
							not null,
	local		 char(30)	descr "Localidad del Cliente.",
	prov 	    char(1)		descr "Provincia"
							in PROVINCIAS : descrip
							not null
							mask ">x",
	cpostal		 num(4)		descr "Codigo postal",
	tipdoc		 char(3)	descr "Tipo de Documento del Cliente.",
	nrodoc		 num (9)	descr "Nro. de Documento del Cliente.",
	area		char(5)		descr "Numero de DDN",
	telefono	char(8)		descr "Numero de telefono",
	vendedor	num(4)		descr "Vendedor"
							not null
							in VENDEDORES : vendnom,
	cajero		num(5)		descr "Cajero",
//							not null,
	movcaja		num(9)		descr "Mov. de Caja Asociado",
	feccot		date		descr "Fecha de Cotizacion",
	horacot		time		descr "Hora de Cotizacion",
	motivo		num(2)		descr "Motivo de Señas",
							//in codifica
	feccanc		date		descr "Fecha de cancelacion de la senia",
	succanc		num(3)		descr "Sucursal donde se realizo la cancelacion",
	cajcanc		num(5)		descr "Cajero que realizo la cancelacion",
	nrocomp     num(9)		descr "Nro. Cbte. de Mov. de Caja",
}
primary key 		 (numcomp) [ 6 ],
unique index SUCURSAL(sucursal, fecha descending, cajero, numcomp) [ 6 ],
unique index FACTURA (tipofac not null, seriefac not null, nrofac not null, numcomp) [ 7 ];

table SENIAPAG 				descr "Financiacion de Señas" {

	numcomp		num(8)		descr "Número de Seña"
							in SENIA : fecha
							not null,
	renglon		num(2)		descr "Número de renglón"
							not null,
	tipo		num(2)		descr "Tipo de Medio de Pago"
							in MEDIOSP : descor
							not null,
	subm		num(3)		descr "Submedios de Pago"
							in SUBMEDIOS (tipo) : descor
							not null,
	cuota		num(2)		descr "Cantidad de Cuotas"
							in CONDMP (tipo, subm) : (descto, tasa)
							not null,
	total		num(10,2)	descr "Total del M. Pago"
							not null,
	capital		num(10,2)	descr "Importe M. de Pago"
							not null,
	rec			num(10,2)	descr "Recargo del Medio de Pago"
							not null,
	descto		num(10,2)	descr "Descuento del M. Pago"
							not null,
	nrocomp		num(8)		descr "Nro de comprobante"
							default null,
	nroauto		char(8)		descr "Numero de Autorizacion de Tarj"
							default null,
	horacot		time		descr "Hora de la Cotizacion",
    feccot		date		descr "Fecha de Cotizacion",
	codmon		num(2)		descr "Moneda relacionada",
							// in aux.moneda
	mctetotal	num(10,2)	descr "Total del M. Pago en moneda corriente"
							not null
							default 0.0,
	mctecap		num(10,2)	descr "Importe M. de Pago en moneda corriente"
							not null
							default 0.0,
	mcterec		num(10,2)	descr "Recargo del Medio de Pago en moneda corriente"
							not null
							default 0.0,
	mctedto		num(10,2)	descr "Descuento del M. Pago en moneda corriente"
							not null
							default 0.0,
	operiso		num(1)		descr "Ultima Operacion (ISO)",
	sectef		num(2)		descr "Numero de secuencia TEF",
}
primary key (numcomp, renglon) [ 4 ];


table COMVEND				descr "Comisiones por Vendedor" {

	vendnum		num(4)		descr "Vendedor"
							not null
							in VENDEDORES : vendnom,
	anio		num(4)		descr "Año"
							not null
							>= 1994,
	mes			num(2)		descr "Mes"
							not null
							between 1 and 12,
	puntos		num(5,2)	descr "Puntos"
							not null
							>= 0,
    comision	num(5,3)	descr "Coeficiente para Comision"
							not null
							>= 0,
}
primary key (vendnum, anio, mes, puntos) [ 2 ],
index ANIO  (anio, mes, vendnum, puntos) [ 2 ];


// OJO con el campo estado, al tener el default 1 si se convierten
// datos de la tabla MOVENT, a todos los que tienen en ese campo
// valor nulo les setea 1.
table MOVENT				descr "Movimientos para entregas del depósito" {

	tipomov		num(2)		descr "Tipo de Movimiento" 
							not null in TIPOMOV:descrip,
	numcomp		num(8)		descr "Nro de Comprobante"
							not null,
	renglon		num(3)		descr "Renglon"
							not null
							in MOVIM (tipomov, numcomp, codadm),
	nmov		num(3)		descr "Nro de Mov."
							>= 0 not null,
	codadm		char(8)		descr "Articulo en garantia"
							not null
							in CATALOGO:(des1adm)
							mask $codadm,
	cantaent	num(9)		descr "Cantidad a entregar"
							not null > 0,
	fecent		date		descr "Fecha de entrega"
							not null,
	horent		num(2)		descr "Horario de entrega"
							in CODIFICA (12) : descrip
							not null,
	estado		num(2)		descr "Estado"
							default 1 // Pendiente de realizar carta de porte
							in CODIFICA (13) : descrip,
	tipent		num(4)		descr "Tipo de comprobante que genera",
	compent		num(8)		descr "Número del comprobante que genera",
	renent		num(3)		descr "Renglón del Comprobante que genera",
	coddir		num(4)		descr "Codigo de direccion",
							//in serv.CTADIR - in SUCURSAL
	deposito	num(3)		descr "Depósito del Artículo"
							not null 
							in SUCURSAL:(descrip),
	clase		num(2)		descr "Clase de Dirección : Dir / Suc"
							in CODIFICA (37) : descrip,
	tipoent		num(2)		descr "Tipo de Entrega"
							in  CODIFICA (221):descrip
							default 2,
								//(1- No entrega
								//2- Entrega Comun
								//3- Entrega Express)
	hdesde		time		descr "Hora desde",
	hhasta		time		descr "Hora hasta",
	remito		num(8)		descr "Remito Interno",
	estentr		num(2)		descr "Estado de Entrega"
							in CODIFICA (362) : descrip,
	cdate		date		descr "Fecha de Carga",
	ctime		time		descr "Hora de Carga",
	cuid		num(5)		descr "Usuario que Cargo",
	mdate		date		descr "Fecha de Modificacion",
	mtime		time		descr "Hora de Modificacion",
	muid		num(5)		descr "Usuario que Modifico",
}
primary key(tipomov, numcomp, renglon, nmov) [8],  /* Base 200.000 */
index ESTADO (estado not null, deposito, fecent, horent, tipomov, numcomp, coddir) [10],
index TIPENT (tipent not null, compent not null, renent not null) [8];

table CODIFICA				descr "Codificadores Generales" {
	tipcod		num(3)		descr "Tipo de Codificador"
//							in TIPCODIF : descrip
							not null,
	codigo		num(3)		descr "Código"
							not null,
	descrip		char(30)	descr "Descripción"
							not null,
	descor		char(10)	descr "Descripción Corta"
							not null,
    valor		num(8)		descr "Valor Asociado",
	tipobs		num(4)		descr "Tipo de Observación",
							//in ventas.sernum
	codobs		num(8)		descr "Numero de Observacion",
	codmon		num(2)		descr "Moneda relacionada",
							// in aux.moneda

}
primary key (tipcod, codigo),
unique index VALOR (tipcod, valor not null, codigo);

table CABPREC 				descr "Cabecera de Precios a Informar" { 
	numcomp		num(8)		descr "Numero de Informe"
							not null,
	sucdest 	num(3)		descr "Sucursal a la cual se informa"
							in SUCURSAL : descrip
							not null,
	sucprec 	num(3)		descr "Sucursal Origen de los Precios"
							in SUCURSAL : descrip
							not null,
	fecha		date		descr "Fecha de Generación del Informe"
							not null default today,
	hora        time        descr "Hora de Generación del Informe"
							not null default hour,
    estado		bool		descr "Estado del Informe"
							not null default true,
}
primary key (numcomp),
unique index ACTIVO (estado, sucdest, fecha, numcomp);

table RENPREC 				descr "Renglones de Precios a Informar" { 
	numcomp		num(8)		descr "Numero de Informe"
							not null in CABPREC,
	codadm		char(8)		descr "Código de administración"
							in CATALOGO:(des1adm)
							not null
							mask $codadm,
	renglon		num(2)		descr "Número de Renglón"
							not null,
}
primary key (numcomp, renglon) [ 2 ];

table CONFNVI				descr "NVIs Confirmadas" {
	tipconf 	num(2)		descr "Tipo de informe de recepción"
							in TIPOMOV
							not null,
	nroconf		num(8)		descr "Número de informe de recepción"
							not null,
	renconf		num(3)		descr "Renglón de informe de recepción"
							not null
							default 0,
	tipnvi	 	num(2)		descr "Tipo de NVI"
							in TIPOMOV
							not null,
	nronvi		num(8)		descr "Número de NVI"
							not null,
	rennvi		num(3)		descr "Renglón de NVI"
							not null
							default 0,
}
primary key (tipconf, nroconf, renconf, tipnvi, nronvi, rennvi);

table ESTXTIP				descr "Estados por Tipo de Movimiento" {
	tipomov		num(2)		descr "Tipo de Movimiento" 
							not null in TIPOMOV:descrip,
	estado		num(2)		descr "Estado",
	scodorig 	num(2)		descr "Estado origen del movimiento"
							in SUBDEP,
	scoddest 	num(2)		descr "Estado de destino del movimiento"
							in SUBDEP,
	deposito	num(3)		descr "Deposito"
							in DEPOSIT : descort,
	priori		num(2)		descr "Prioridad",
}
primary key (tipomov, estado),
index PRIORIDAD (priori not null, tipomov);
			
table REMITOS				descr "Remitos de Facturas" {
	codprov   	num(6)		descr "Código de Proveedor" 
							not null,
	serie	  	num(4)		descr "Serie del Remito" 
							not null,
	numero    	num(8)		descr "Número de Remito" 
							not null,
	tipomov   	num(2)		descr "Tipo de Movimiento Asociado"
							not null
							in TIPOMOV:descrip,
	numcomp     num(9)  	descr "Número de Comprobante"
							not null,
	tipccte		num(2)		descr "Tipo de Cuenta Corriente del Proveedor"
							not null
							default 1,
}
primary key (tipomov, numcomp, serie, numero) [ 6 ],
unique index REMITO (tipccte, codprov, serie, numero, tipomov, numcomp) [ 8 ];

table TCMOVREL				descr "Relaciones entre Comprobantes de COMPRAS por Operación" {
	tipcomp		num(4)		descr "Tipo de cbte de AURUS"
								not null,
    tipoper     num(2)      descr "Tipo de Operación"
								in CODIFICA(7) : descrip not null,
	tipcrel		num(4)		descr "Tipo de Comprobante de Aurus relacionado"
								not null,
}
primary key (tipcomp, tipoper, tipcrel);

table FLAGSUC				descr "Flags de sucursales" {

	suc			num(3)		descr "Sucursal"
							in SUCURSAL : descor
							not null,
	flag		num(2)		descr "Flag"
							in CODIFICA(8) : descrip
							not null,
	valor		num(4)		descr "Valor"
							not null,
}
primary key (suc, flag),
unique index FLAG (flag, valor, suc);

table SERNUM				descr "Serie de Numeración" {

	serie		num(4)		descr "Serie"
							not null
							>0,     
	descrip		char(30)	descr "Descripción"
									not null,
	descor		char(10)	descr "Descripción Corta"
									not null,
	ultasig		num(8)		descr "Ultimo Número Asignado" 
									default 0
									not null,
	nromin		num(8)		descr "Número Mínimo Permitido"    
									default 1 
									not null
									> 0, 
	nromax		num(8)		descr "Número Máximo Permitido"   
									default 99999999 
									not null
									> 0, 
	lote		num(8)		descr "Cantidad de Valores por Lote",
}
primary key (serie);

table VALMXF				descr "Valores por Marca y Familia" {

	tipo		num(3)		descr "Tipo de Valor"
							in TIPVAL : descrip
							not null,
	suc			num(3)		descr "Sucursal"
							in SUCURSAL : descor
							not null,
	marca		num(8)		descr "Código de marca"
							in MARCA : descrip
							not null,
	codflia		char(8)		descr "Código de familia"
							in FAMILIA : des1flia
							not null
							mask $codflia,
	fdesde		date		descr "Fecha desde"
							not null,
	fhasta		date		descr "Fecha hasta",
	valor		num(10,2)	descr "Valor"
							not null,

}
primary key (tipo, suc, marca, codflia, fdesde);

table TIPCODIF				descr "Tipos Codificadores Generales" {
	tipcod		num(3)		descr "Tipo de Codificador"
							not null,
	descrip		char(30)	descr "Descripción"
							not null,
}
primary key (tipcod);

table AUTOIMP				descr "Comprobantes emitidos por autoimpresión" {
	tipomov		num(2)		descr "Tipo de Comprobante"
							not null,
	numcomp		num(8)		descr "Número de Comprobante"
							not null,
	tipo		char(1)		descr "Tipo de Factura"
							// in serv.SERFAC
							not null,
	serie		char(4)		descr "Nro. de Serie"
							not null,
	nro			char(8)		descr "Nro."
							not null,
	carpor		num(8)		descr "Número de Carta de Porte",
	tipent		num(2)		descr "Tipo de comprobante relacionado",
	compent		num(8)		descr "Nro. de comprobante relacionado",
	fecha		date		descr "Fecha del comprobante - autoimpresion"
							not null,
	trans		num(9)		descr "Transportista",
	estado		num(2)		descr "Estado",
	cdate		date		descr "Fecha de Carga",
	ctime		time		descr "Hora de Carga",
	cuid		num(5)		descr "Usuario que Cargo",
	mdate		date		descr "Fecha de Modificacion",
	mtime		time		descr "Hora de Modificacion",
	muid		num(5)		descr "Usuario que Modifico",
}

primary key (tipomov, numcomp) [12],  /*Base 1.000.000 */
unique index SERIE (tipo, serie, nro, tipomov, numcomp) [28];

table CBFISC 		  			descr "Cabecera de Cbte. Fiscal" {
	tipfis		num(2)		descr "Tipo de CBTE Fiscal"
							not null,
							// in codifica 114
	nrofis		num(8)		descr "Nro de CBTE Fiscal"
							not null,
	fecha		date		descr "Fecha de Generacion"
							default today,
	sucursal    num(3)      descr "Sucursal"
							in SUCURSAL : descor
							not null,
	tipon		char(1)		descr "Tipo de Nota Fiscal"
							in ("A":"Fc/ND/NC Tipo A",
								"B":"Fc/ND/NC Tipo B",
								"E":"Fc/ND/NC Tipo E",
								"X":"Recibo X",
								"Z":"Recibo B")
							mask ">x",
	serien		char(4)		descr "Sucursal Nota Fiscal"
							mask "4n",
	nron		char(8)		descr "Nota Fiscal Nro."
							mask "8n",
	nombre      char(30)   descr "Nombre del Cliente.",
	direccion	 char(30)	descr "Dirección del Cliente."
							not null,
	local		 char(30)	descr "Localidad del Cliente.",
	prov		char(1)		descr "Provincia"
							in PROVINCIAS : descrip
							not null
							mask ">x",
	cuit		char(15)	descr "C.u.i.t. del Cliente.",
	bruto		num(11,2)	descr "Bruto de Nota Fiscal."
							not null
							default 0,
	neto		num(11,2)	descr "Neto de Nota Fiscal."
							not null
							default 0,
	tipoiva		 num(2)		descr "Tipo de Iva del Cliente."
							not null
							in TIVA by VENTAS(true) : descrip
				      		default 1,
	poriva		num(4,2)	descr "Porcentaje de Iva"
							not null
							default 0,
	iva			num(11,2)	descr "Iva de N. Vta."
							not null
							default 0,
	discr		bool		descr "Discrimina Iva"
							not null default false,
	cpostal		 num(4)		descr "Codigo postal",
	tipdoc		 char(3)	descr "Tipo de Documento.",
	nrodoc		 num (9)	descr "Nro. de Documento.",
	poriva1		num(4,2)	descr "Porcentaje de Iva 1"
							not null
							default 0,
	iva1		num(11,2)	descr "Iva 1 de N. Cred."
							not null
							default 0,
	rg3337		bool		descr "Se le realiza RG3337"
							not null
							default false,
	piva3337	num(4,2)	descr "Porcentaje de Iva RG3337"
							not null
							default 0,
	iva3337		num(11,2)	descr "Iva RG3337"
							not null
							default 0,
	fecval		date		descr "Fecha de Valor / Comprobante",
	estado		char(1)		descr "Estado de N. Debito"
     						in ("C":"Contabilizada",
								"F":"Facturada",
								"I":"Pendiente de Impresion",
								"S":"Impresa Sin Reimpresion CF",
								"Z":"CF - Error",
								"N":"Anulada")
							default "F"
							mask ">A",
	codtar		num(2)		descr "Codigo",
	tipo		num(2)		descr "Tipo de Comprobante",
	nrocomp		num(8)		descr "Número de Comprobante",
	renglon		num(2)		descr "Renglón",
	horacot		time		descr "Hora de la Cotizacion",
    feccot		date		descr "Fecha de Cotizacion",
	ngravado  	num(11,2)	descr "Monto No Gravado",
}
primary key (tipfis, nrofis) [16], /* Base 1.700.000 */
unique index FECVAL (fecval, tipfis, nrofis) [20],
unique index NUMERA (tipon not null, serien not null, nron not null, tipfis, nrofis) [40],
unique index tpropia(codtar not null, tipo not null, nrocomp not null, renglon not null, tipfis, nrofis) [29],
unique index SUCURSAL(sucursal, estado, tipfis, nrofis) [20];

table ESTFUN				descr "Estados válidos por función" {
	tipomov		num(2)		descr "Tipo de Movimiento" 
							not null in TIPOMOV:descrip,
	funcion		num(2)		descr "Función"
							in CODIFICA(122):descrip,
	estado		char(1)		descr "Condición de aprobación del movimiento"
     						in CODIFC(1):descrip
							mask "X"
							not null,
	verif		bool		descr "Verifica ?"
							not null default false,
}
primary key (tipomov, funcion, estado);

table CODIFC				descr "Codificadores Generales con clave char" {
	tipcod		num(2)		descr "Tipo de Codificador"
							in CODIFICA(124) : descrip
							not null,
	codigo		char(4)		descr "Código"
							not null,
	descrip		char(30)	descr "Descripción"
							not null,
	descor		char(10)	descr "Descripción Corta"
							not null,
    valor		num(10,2)	descr "Valor Asociado",
							
}
primary key (tipcod, codigo);

table PSEPAR				descr "Parametros de separacion" {

	codigo		num(2)		descr "Codigo"
							not null,
	descrip		char(30)	descr "Descripcion"
							not null,
	valor		char(10)	descr "Valor"
							not null,
}
primary key (codigo);

table CABORD 				descr "Cabecera de las Ordenes de compra" {
	tipomov		num(2)		descr "Tipo de Orden "
							not null,
	numero		num(8)		descr "Número de Orden"
							not null,
	tipcot		num(2)		descr "Tipo de Cotización",
	refnot		char(20)	descr "Referencia a la Nota de Pedido del Proveedor",
	estado		num(2)		descr "Estado",
	termino		num(3)		descr "Codigo de Término de Pago"
							in CODIFICA (336),
	fentval		date		descr "Fecha de entrega de Valores (para EBS)",
	// Anulacion de OC y de IR
	fecanula	date		descr "Fecha de Anulacion",
	horanula	time		descr "Hora de Anulacion",
	empleg		num(2) 		descr "Empresa del Legajo que Anula",
	nroleg		num(9)		descr "Nro. Legajo del que Anula",
	// Relacion con OC desde IR 
	tipent		num(2)		descr "Tipo de comprobante relacionado",
	compent		num(8)		descr "Nro. de comprobante relacionado",
	// Tipo de Compra (campo tipcomp en CATALOGO)
	tcompra     num(2)      descr "Tipo de Compra"
							in CODIFICA (212)
							default 1
 							not null,
	// TimeStamp
	cdate		date		descr "Fecha de Carga",
	ctime		time		descr "Hora de Carga",
	cuid		num(5)		descr "Usuario que Cargo",
	mdate		date		descr "Fecha de Modificacion",
	mtime		time		descr "Hora de Modificacion",
	muid		num(5)		descr "Usuario que Modifico",
}
primary key	(tipomov, numero) [10]; // Calculado en base a 500000 registros 

table PROMOS  				descr "Promociones p/descuentos" {
	promo 		num(3)		descr "Tipo de Promocion"  
  							in CODIFICA(157):descrip
							not null,
	motivo		num(2)		descr "Código de Motivo"
  							in CODIFICA(108):descrip,
	porcmax		num(5,2)	descr "Porc. Maximo de Dto."
							default 0
							not null,
	porcban		num(5,2)	descr "Porc. a cargo del Banco"
							default 0
							not null,
    validx25    bool        descr "Valida X25"
                            default true 
                            not null,
	tipbin		num(2)		descr "Tipo de Bin"
							in CODIFICA(127):descrip,
	fdesde		date		descr "Fecha de Vigencia Desde"
                            default today
    						not null,
	fhasta		date		descr "Fecha de Vigencia Hasta",
	porsuc		bool		descr "Promo por Sucursal"
                            default false 
                            not null,
    valbin      num(1)      descr "Valida Bin?"
    						in (0:"No Valida",1:"Valida Bin",2:"Por Entidad",3:"Por Nro. Cuenta")
                            default 0
                            not null,
	porart 		bool		descr "Promo por Articulo"
                            default false 
                            not null,
	limite		num(12,2)	descr "Limite de Promo"
							default 0,	
    tiplim      num(1)      descr "Tipo de Limite"
    						in (0:"Mensual",1:"Por Operacion")
                            default 0
                            not null,
	hdesde		time		descr "Hora desde"
							default "00:00:00"
							not null,
	hhasta		time		descr "Hora hasta",
	entidad		num(3)		descr "Entidad Bancaria",
							// in ddirect.DDENTI
	tipncta		num(3)		descr "Tipo Numero de cuenta"
							in CODIFICA(314):descrip,
	tipprom     num(1)		descr "Tipo de promocion"
							in (1:"Promo por medio de pago", 2:"Promo por tarjeta de descuento")
							default 1
							not null,
	ctrlmpag	num(1)		descr "Control medios de pago"
							in (0:"Sin Control", 1:"Control por inclusión", 2:"Control por exclusión")
							default 0
							not null,							
	tipmpag		num(3)		descr "Tipo valor medio de pago relacionado"
							in CODIFICA(119):descrip,
}
primary key (promo, motivo),
index MOTIVO (motivo, fdesde),
index FECHA (fhasta);

table BLOQCAT				descr "Bloqueo movimientos de stock por catalogo" {
	codadm		char(8)		descr "Código de administración"
							in CATALOGO:(des1adm)
							not null
							mask $codadm,
	mdate		date		descr "Fecha de Modificacion",
	mtime		time		descr "Hora de Modificacion",
	muid		num(5)		descr "Usuario que Modifico",
}
primary key (codadm);

/****************************************************************************/
/*                                                                          */
/* No Agregar Tablas a este SCHEMA, la idea es utilizar el schema vengral,  */
/* el cual por naturaleza esta compartido por Garbarino y Compumundo        */
/* Su origen es multi-empresa, por lo tanto en caso de ser necesario se     */
/* debera incluir en la tabla nueva el campo "nroemp"                       */
/*                                                                          */
/****************************************************************************/

grant alter on schema ventas to public; 
grant all on * to public with grant option;
