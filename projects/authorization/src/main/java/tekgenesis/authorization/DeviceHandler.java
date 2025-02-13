
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package tekgenesis.authorization;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.util.ByteSource;
import org.jetbrains.annotations.NotNull;

import tekgenesis.form.ApplicationContext;
import tekgenesis.persistence.InnerEntitySeq;
import tekgenesis.service.Factory;
import tekgenesis.service.Result;

import static java.lang.String.format;

import static tekgenesis.authorization.g.DeviceTable.DEVICE;
import static tekgenesis.common.env.context.Context.getSingleton;

/**
 * User class for Handler: DeviceHandler
 */
public class DeviceHandler extends DeviceHandlerBase {

    //~ Constructors .................................................................................................................................

    DeviceHandler(@NotNull Factory factory) {
        super(factory);
    }

    //~ Methods ......................................................................................................................................

    @NotNull @Override public Result<String> register(@NotNull DeviceInfo deviceInfo) {
        final tekgenesis.metadata.authorization.User user = getUser();

        final Device device = Device.findWhere(DEVICE.DEVICE_ID.eq(deviceInfo.getId()));
        // unregister
        if (device != null) device.delete();

        final User userDb = User.find(user.getId());

        if (userDb == null) return badRequest();

        final InnerEntitySeq<Device> devices   = userDb.getDevices();
        final Device                 newDevice = devices.add();
        newDevice.setName(deviceInfo.getName());
        newDevice.setDeviceId(deviceInfo.getId());

        // noinspection MagicNumber
        final String      token;
        final String      appId = getAppId(deviceInfo.getName(), user);
        final Application app   = Application.find(appId);
        if (app == null) {
            final Application application = Application.create(appId);
            // noinspection MagicNumber
            token = new Sha256Hash(deviceInfo.getId(), ByteSource.Util.bytes(user.getId()), 512).toBase64();
            application.setToken(token);
            application.setUser(userDb);
            application.insert();
        }
        else token = app.getToken();

        userDb.persist();
        return ok(token);
    }

    @NotNull @Override public Result<Void> unregister(@NotNull String deviceId) {
        final tekgenesis.metadata.authorization.User user = getUser();

        final Device device = Device.findWhere(DEVICE.DEVICE_ID.eq(deviceId));
        if (device != null) {
            if (device.getUserId().equals(user.getId())) {
                device.delete();
                final Application application = Application.find(getAppId(device.getName(), user));
                if (application != null) application.delete();
                return ok();
            }
            else return unauthorized();
        }
        return notFound();
    }

    private String getAppId(String deviceName, tekgenesis.metadata.authorization.User user) {
        return format("%s-%s", user.getId(), deviceName);
    }

    private tekgenesis.metadata.authorization.User getUser() {
        final ApplicationContext context = getSingleton(ApplicationContext.class);
        return context.getUser();
    }
}
