
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.common.tools.test.docker;

import org.junit.rules.ExternalResource;

/**
 * Docker container rule.
 */
public class DockerContainerRule extends ExternalResource {
    // //~ Instance Fields
    // ..............................................................................................................................
    //
    // DockerClient                     docker;
    // private final CreateContainerCmd container;
    // private String                   containerId              = null;
    // private InspectContainerResponse inspectContainerResponse = null;
    //
    // //~ Constructors
    // .................................................................................................................................
    //
    // /** Constructor with container id and exposed ports. */
    // public DockerContainerRule(String containerId, Seq<ExposedPort> exposedPorts) {
    // docker = createDockerClient();
    //
    // final Seq<PortBinding> bindings = exposedPorts.map(e -> new PortBinding(new Ports.Binding(), e));
    //
    // docker.pullImageCmd(containerId).exec(new PullImageResultCallback()).awaitSuccess();
    // container =
    // docker.createContainerCmd(containerId)
    // .withExposedPorts(exposedPorts.toArray(ExposedPort.class))
    // .withPortBindings(bindings.toArray(PortBinding.class));
    // }
    //
    // //~ Methods
    // ......................................................................................................................................
    //
    // /** Return Host:Port for port binding. */
    // public String getHostPortBinded(ExposedPort port) {
    // return getDockerHost() + ":" + inspectContainerResponse.getNetworkSettings().getPorts().getBindings().get(port)[0].getHostPort();
    // }
    //
    // @Override protected void after() {
    // super.after();
    // docker.killContainerCmd(containerId).exec();
    // docker.removeContainerCmd(containerId).exec();
    // }
    // @Override protected void before()
    // throws Throwable
    // {
    // super.before();
    //
    // containerId = container.exec().getId();
    // docker.startContainerCmd(containerId).exec();
    //
    // inspectContainerResponse = docker.inspectContainerCmd(containerId).exec();
    // }
    //
    // private DockerClient createDockerClient() {
    // final DockerClientConfig.DockerClientConfigBuilder dockerClientConfigBuilder =
    // DockerClientConfig.createDefaultConfigBuilder()
    // .withUri(DOCKER_MACHINE_SERVICE_URL)
    // .withDockerCertPath(System.getProperty("user.home") + "/.docker/machine/certs");
    //
    // final DockerClientConfig config = dockerClientConfigBuilder.build();
    // return DockerClientBuilder.getInstance(config).build();
    // }
    //
    // private String getDockerHost() {
    // try {
    // return new URI(DOCKER_MACHINE_SERVICE_URL).getHost();
    // }
    // catch (final URISyntaxException e) {
    // return Constants.LOCALHOST;
    // }
    // }
    //
    // //~ Static Fields
    // ................................................................................................................................
    //
    // private static final String TEK_DOCKER                 = "http://10.0.1.105:2376";
    // public static final String  DOCKER_MACHINE_SERVICE_URL = System.getProperty("docker.url", TEK_DOCKER);
}  // end class DockerContainerRule
