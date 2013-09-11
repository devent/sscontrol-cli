package maradns.ubuntu_10_04

profile "ubuntu_10_04", {
	hostname { }
	hosts { }
	dns { service "maradns" }
	dhclient { }
}
