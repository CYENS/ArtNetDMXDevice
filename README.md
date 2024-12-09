# ArtNetDMXDevice
A class to be used as a DMXDevice to stream DMX data using the ArtNet protocol. 

# Install
In a SuperCollider file (.scd) run:
```
Quarks.install("https://github.com/CYENS/ArtNetDMXDevice.git")
```

# Example usage
```
d = DMX.new;
e = ArtNetDMXDevice.new("127.0.0.1", 6454, normalised: true, showDebug: true);
// e = TestDMXDevice.new("127.0.0.1", 6454, true);
d.device = e;
c = DMXCue.new;
([1, 98,100,102]-1).do{ |it| c.put(it, 0.5) };
d.currentCue = c;
d.setCue; // This should send an Art-Net packet over UDP
```
```
// When data are between [0..255]
d = DMX.new;
e = showDebug
d.device = ArtNetDMXDevice.new("127.0.0.1", 6454, normalised: false, showDebug: true);
c = DMXCue.new;
([1, 98,100,102]-1).do{ |it| c.put(it, 250) };
d.currentCue = c;
d.setCue; // This should send an Art-Net packet over UDP
```
