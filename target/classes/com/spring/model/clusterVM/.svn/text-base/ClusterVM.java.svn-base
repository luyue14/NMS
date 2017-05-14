package com.spring.model.clusterVM;

import java.util.ArrayList;
import java.util.List;
import com.spring.model.clusterVM.ClusterInfo;

public class ClusterVM {

	private String name;
    private long timeStamp;
	  
	private List<ClusterInfo> clusterInfo = new ArrayList<ClusterInfo>();

    /**
     * 
     * @return
     *     The clusterInfo
     */
    public List<ClusterInfo> getClusterInfo() {
        return clusterInfo;
    }

    /**
     * 
     * @param clusterInfo
     *     The cluster_info
     */
    public void setClusterInfo(List<ClusterInfo> clusterInfo) {
        this.clusterInfo = clusterInfo;
    }

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getTimeStamp() {
		return timeStamp;
	}
    @Override
  	public int hashCode() {
  		final int prime = 31;
  		int result = 1;
  		result = prime * result + ((clusterInfo == null) ? 0 : clusterInfo.hashCode());
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
  		ClusterVM other = (ClusterVM) obj;
  		if (clusterInfo == null) {
  			if (other.clusterInfo != null)
  				return false;
  		} else if (!clusterInfo.equals(other.clusterInfo))
  			return false;
  		return true;
  	}

  	@Override
  	public String toString() {
  		return "Cluster "+"name"+name+"timestamp"+timeStamp+"[clusterInfo=" + clusterInfo + "]";
  	}
  	
}
