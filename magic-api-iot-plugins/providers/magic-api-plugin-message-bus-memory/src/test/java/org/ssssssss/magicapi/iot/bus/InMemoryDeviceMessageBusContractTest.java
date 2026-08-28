package org.ssssssss.magicapi.iot.bus;
import org.ssssssss.magicapi.iot.core.spi.DeviceMessageBus; import org.ssssssss.magicapi.iot.testkit.MessageBusContract;
class InMemoryDeviceMessageBusContractTest extends MessageBusContract { private final DeviceMessageBus value=new InMemoryDeviceMessageBus(); protected DeviceMessageBus messageBus(){return value;} }
