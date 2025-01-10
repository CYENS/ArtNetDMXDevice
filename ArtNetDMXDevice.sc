ArtNetDMXDevice {
    var <>addr, <>port, <>normalised, <>showDebug;

    *new { |addr="127.0.0.1", port=6454, normalised=false, showDebug=false|
        ^super.new.init(addr, port, normalised, showDebug)
    }

    init { |addr, port, normalised, showDebug|
        this.addr = addr;
        this.port = port;
        this.normalised = normalised;
		this.showDebug = showDebug;
    }

    sendDMX { |cue|
        var data, artnetPacket, artnetAddr;

        // If normalised is true, scale data from [0..1] to [0..255]
		// If not normalised, we assume data is already 0–255 integers.
        data = cue.data;
		data = if (normalised,
			{ data.collect { |val| (val * 255).clip(0, 255).asInteger } },
			{ data }
		);

        artnetPacket = this.makeArtNetPacket(data);
        artnetAddr = NetAddr(addr, port);

		if(showDebug,
			{ ("[ArtNetDMXDevice " ++ addr ++":" ++ port ++ "] DMX Data (channel, value): " ++ this.collectGreaterThanZero(data)).postln },
			{}
		);

        artnetAddr.sendRaw(artnetPacket);
    }

    makeArtNetPacket { |dmxDataArray|
        var header, opcode, protVer, sequence, phys, universe, lengthHi, lengthLo, length, packetArray;
        // "Art-Net" header + null terminator
        header = "Art-Net".ascii ++ [0];
        // OpCode for ArtDmx is 0x5000 (little endian: 0x00, 0x50)
        opcode = [0x00, 0x50];
        // Protocol version (0x000e = 14)
        protVer = [0x00, 0x0e];
        // Sequence (0x00), Physical port (0x00)
        sequence = [0x00];
        phys = [0x00];
        // Universe (low byte, high byte) - set to 1,0 here
        universe = [0x01, 0x00];
        // Length high, length low
        lengthHi = (dmxDataArray.size >> 8).bitAnd(0xFF);
        lengthLo = dmxDataArray.size.bitAnd(0xFF);
        length = [lengthHi, lengthLo];

        packetArray = header ++ opcode ++ protVer ++ sequence ++ phys ++ universe ++ length ++ dmxDataArray;

        ^packetArray.as(Int8Array)
    }

	collectGreaterThanZero { |array|
        var tuples = array.collect { |val, i|
            if (val > 0) {
                "(" ++ i ++ ", " ++ val ++ ")"
            } {
                nil
            }
        };
        tuples = tuples.reject { |x| x.isNil };
        ^"[" ++ tuples.join(", ") ++ "]"
    }
}
