package tekgenesis.sg;

entity ClusterConf "Cluster Conf"
	primary_key name
    described_by name
    searchable
    auditable
{
    name:String;
    entryPoints: entity HostAddress * {
        address:String;
    };
    descr:String;
    status: ClusterStatus, default DEACTIVE;
}

enum ClusterStatus
{
    ACTIVE;
    DEACTIVE;
}

entity ClusterStats
{
    cluster : ClusterConf;
    time: DateTime;
    stats: String(8192);
}


entity NodeEntry "Node Status"
    primary_key name
    described_by name
    auditable
{
    name:String;
    status:MemberStatus;
    log:String(8192);

}

enum MemberStatus
{
   FAILED;
   READY;
   RESTARTING;
   STOP;
   UPDATED;
   UPDATING;
   SAFE_MODE;
}