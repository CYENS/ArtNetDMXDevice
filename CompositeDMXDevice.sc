CompositeDMXDevice {
    var <>devices; // Array of DMX devices

    *new { |devices|
		devices = devices ?? []; // Use an empty array if `devices` is not provided
        ^super.new.init(devices);
    }

    init { |devices|
        this.devices = devices;
    }

    addDevice { |device|
        devices = devices.add(device);
    }

    removeDevice { |device|
        devices = devices.reject { |d| d === device };
    }

    sendDMX { |cue|
        devices.do { |device|
            device.sendDMX(cue);
        };
    }
}
