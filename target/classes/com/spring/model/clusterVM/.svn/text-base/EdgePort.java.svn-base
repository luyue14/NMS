package com.spring.model.clusterVM;

import java.util.ArrayList;
import java.util.List;

public class EdgePort {
    private String dpId;
    private List<String> dpPort = new ArrayList<String>();
    private List<String> macAddress = new ArrayList<String>();


	private List<String> ipAddress = new ArrayList<String>();
    private List<String> network = new ArrayList<String>();

    /**
     * 
     * @return
     *     The dpId
     */
    public String getDpId() {
        return dpId;
    }

    /**
     * 
     * @param dpId
     *     The dp_id
     */
    public void setDpId(String dpId) {
        this.dpId = dpId;
    }

    /**
     * 
     * @return
     *     The dpPort
     */
    public List<String> getDpPort() {
        return dpPort;
    }

    /**
     * 
     * @param dpPort
     *     The dp_port
     */
    public void setDpPort(List<String> dpPort) {
        this.dpPort = dpPort;
    }

    /**
     * 
     * @return
     *     The macAddress
     */
    public List<String> getMacAddress() {
        return macAddress;
    }

    /**
     * 
     * @param macAddress
     *     The mac_address
     */
    public void setMacAddress(List<String> macAddress) {
        this.macAddress = macAddress;
    }
    
    
    public List<String> getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(List<String> ipAddress) {
		this.ipAddress = ipAddress;
	}

	public List<String> getNetwork() {
		return network;
	}

	public void setNetwork(List<String> network) {
		this.network = network;
	}

	@Override
	public String toString() {
		return "EdgePort [dpId=" + dpId + ", dpPort=" + dpPort + ", macAddress=" + macAddress + ",ipAddress="+ipAddress+",network="+network+"]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dpId == null) ? 0 : dpId.hashCode());
		result = prime * result + ((dpPort == null) ? 0 : dpPort.hashCode());
		result = prime * result + ((macAddress == null) ? 0 : macAddress.hashCode());
		result = prime * result +  ((ipAddress == null) ? 0 : ipAddress.hashCode());
		result = prime * result +  ((network == null) ? 0 : network.hashCode());
		
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EdgePort other = (EdgePort) obj;
		if (dpId == null) {
			if (other.dpId != null)
				return false;
		} else if (!dpId.equals(other.dpId))
			return false;
		if (dpPort == null) {
			if (other.dpPort != null)
				return false;
		} else if (!dpPort.equals(other.dpPort))
			return false;
		if (macAddress == null) {
			if (other.macAddress != null)
				return false;
		} else if (!macAddress.equals(other.macAddress))
			return false;
		if (ipAddress == null) {
			if (other.ipAddress != null)
				return false;
		} else if (!ipAddress.equals(other.ipAddress))
			return false;
		if (network == null) {
			if (other.network != null)
				return false;
		} else if (!network.equals(other.network))
			return false;
		return true;
	}
    
    

}
