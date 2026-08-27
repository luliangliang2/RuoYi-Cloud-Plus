package org.ssssssss.magicapi.iot.core.spi;

import org.ssssssss.magicapi.iot.core.model.DeviceIdentity;

import java.util.List;
import java.util.Set;

/** Administrative device operations kept separate from the runtime authentication contract. */
public interface DeviceRegistryAdmin {

    RegisteredDevice register(RegisteredDevice device);

    RegisteredDevice update(RegisteredDevice device);

    void delete(DeviceIdentity identity);

    DevicePage search(String productId, String keyword, int page, int pageSize);

    RegisteredDevice setEnabled(DeviceIdentity identity, boolean enabled);

    Set<String> credentialTypes(DeviceIdentity identity);

    void setCredential(DeviceIdentity identity, DeviceCredential credential);

    void deleteCredential(DeviceIdentity identity, String credentialType);

    boolean verifyCredential(DeviceIdentity identity, DeviceCredential credential);

    record DevicePage(List<RegisteredDevice> items, long total, int page, int pageSize) {
        public DevicePage {
            items = items == null ? List.of() : List.copyOf(items);
        }
    }
}
