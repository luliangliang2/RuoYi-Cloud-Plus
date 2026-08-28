package org.ssssssss.magicapi.iot.registry;
import org.ssssssss.magicapi.iot.core.model.DeviceIdentity; import org.ssssssss.magicapi.iot.core.spi.*; import org.ssssssss.magicapi.iot.testkit.DeviceRegistryContract;
class InMemoryDeviceRegistryContractTest extends DeviceRegistryContract { private final InMemoryDeviceRegistry value=new InMemoryDeviceRegistry(); protected DeviceRegistry registry(){return value;} protected void setCredential(DeviceIdentity id,DeviceCredential credential){value.setCredential(id,credential);} }
