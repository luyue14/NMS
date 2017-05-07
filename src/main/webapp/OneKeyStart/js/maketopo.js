﻿			$(document).ready(function() {
				var canvas = document.getElementById("canvas"); 
				var stage = new JTopo.Stage(canvas);
				var scene = new JTopo.Scene();				
				var configResult;
				var clusterResult;
				var stateResult = 1;
				var topoResult;
				var configBack;
				var clickNodeInConfig = -1;
				var nodes = [];
				var clusterNum = 0;
				var newCluster = new Object();
				var widthOfCanvas = $(document).width()-325 - 17;
				var heightOfCanvas = $(document).height()-2;
				var widthOfImage = Math.sqrt(widthOfCanvas*heightOfCanvas/840000)*60;
				var heightOfImage = Math.sqrt(widthOfCanvas*heightOfCanvas/840000)*50;
				var isNew = true;
				var lastPop = null;
				
				$("#canvas").attr('width', widthOfCanvas);
				$("#canvas").attr('height', heightOfCanvas);
				scene.mode = "edit";
				stage.add(scene);
				newCluster.clusterInfo = new Array();
				
				function add2Node(nodeName, nodeInfo, x, y, nodePositionInConfig){
					var node = new JTopo.Node(nodeName);
					node.id = nodeInfo.nodeId;
					node.setImage("images/switchNew.png", false); 
					node.setSize(widthOfImage,heightOfImage);
					node.setLocation(x, y);
					node.fontColor = "0,0,0";
					node.zIndex = 999; 
					node.mousedrag(function(){
						//拖拽对config修改；
						configResult.node[nodePositionInConfig].xCoordinate = node.getLocation().x;
						configResult.node[nodePositionInConfig].yCoordinate = node.getLocation().y;
						
					})
					node.mouseup(function(){
						//鼠标松开判断是否属于任何一个cluster，从位置上判断属于哪个cluster
						for(var i = 0; i < configResult.node.length; i++){
							if(isContains(configResult.node[i].nodeId,"cluster")){
								if((node.getLocation().x > configResult.node[i].xCoordinate)
								&&(node.getLocation().x < configResult.node[i].xCoordinate + configResult.node[i].width)
								&&(node.getLocation().y > configResult.node[i].yCoordinate)
								&&(node.getLocation().y < configResult.node[i].yCoordinate + configResult.node[i].height)){//判断最终位置属于哪个cluster
									for(var j = 0; j < newCluster.clusterInfo.length; j++){//在cluster表找那个找到对应的分簇；
										if("cluster" + newCluster.clusterInfo[j].clusterId == configResult.node[i].nodeId){
											var dpListTag = false;
											for(var k = 0; k < newCluster.clusterInfo[j].dpList.length; k++){//判断dplist，用于查重；
												if(newCluster.clusterInfo[j].dpList[k] == node.id){
													dpListTag = true;
													break;
												}
											}
											if(dpListTag == false){
												newCluster.clusterInfo[j].dpList.push(node.id);
												alert(node.id + "成功添加到" + configResult.node[i].nodeName);
											}
										}
									}
								}else{//判断移出该簇；
									for(var j = 0; j < newCluster.clusterInfo.length; j++){
										if("cluster" + newCluster.clusterInfo[j].clusterId == configResult.node[i].nodeId){
											if($.inArray(node.id,newCluster.clusterInfo[j].dpList) != -1){
												newCluster.clusterInfo[j].dpList.splice($.inArray(node.id,newCluster.clusterInfo[j].dpList),1);
												alert(node.id + "成功移出cluster" + configResult.node[i].nodeName);
											}
										}
									}
								} 
							}
						//break;//只允许一个交换机属于一个分簇，防止多次添加；
						}
					})
					node.click(function(){
						clickNodeInConfig = nodePositionInConfig;
					})
					node.dbclick(function() {
						window.open("flowtable.html?id="+node.id);
					})
					scene.add(node);
					return node;
				}
				function add3Node(nodeName, nodeInfo, x, y, nodePositionInConfig){
					var node = new JTopo.Node(nodeName);
					node.id = nodeInfo.nodeId;
					node.setImage("images/host.png", false);
					node.setSize(widthOfImage,heightOfImage);
					node.setLocation(x, y);
					node.fontColor = "0,0,0";
					node.zIndex = 999; 
					node.mousedrag(function(){
						configResult.node[nodePositionInConfig].xCoordinate = node.getLocation().x;
						configResult.node[nodePositionInConfig].yCoordinate = node.getLocation().y;
					})
					node.click(function(){
						clickNodeInConfig = nodePositionInConfig;
					})
					scene.add(node);
					return node;
				}
				function addCluster(nodeName, x, y,clusterPositionInConfig){//用于添加分簇的背景图；
					var node = new JTopo.Node(nodeName);
					var xStartToMove;//用于标识cluster开始移动的起始位置；
					var yStartToMove;
					var sonX = [];//属于该簇的孩子坐标数组；
					var sonY = [];
					var positionInNodes = [];//属于该簇的孩子在nodes数组中位置的数组；
					var isMouseUp = true;
					node.setImage("images/cloud.png", false);
					node.setSize(widthOfImage *(2 * 2 + 1),heightOfImage *(2 * 2 + 1));
					node.setLocation(x, y);
					node.fontColor = "0,0,255";
					node.font = "18px Consolas"
					node.zIndex = 10; 
					node.shadow = false;
					node.mousedown(function(){
						isMouseUp = false;
						xStartToMove = node.getLocation().x;
						yStartToMove = node.getLocation().y;
						for(var i = 0; i < newCluster.clusterInfo.length; i++){
							if("cluster" + newCluster.clusterInfo[i].clusterId == nodeName){
								for(var j = 0; j < newCluster.clusterInfo[i].dpList.length; j++){
									for(var k = 0; k < nodes.length; k++){
										if(newCluster.clusterInfo[i].dpList[j] == nodes[k].id){
											sonX[j] = nodes[k].entity.getLocation().x;
											sonY[j] = nodes[k].entity.getLocation().y;
											positionInNodes[j] = k;
										break;
										}
									}									
								}
								break;
							}
						}
						
					})
					node.mouseup(function(){
						isMouseUp = true;
						configResult.node[clusterPositionInConfig].width = node.getBound().width;
						configResult.node[clusterPositionInConfig].height = node.getBound().height;
						configResult.node[clusterPositionInConfig].xCoordinate = node.getLocation().x;
						configResult.node[clusterPositionInConfig].yCoordinate = node.getLocation().y;
						//console.log(configResult.node[clusterPositionInConfig].width);
					})
					node.mousedrag(function(){
						configResult.node[clusterPositionInConfig].xCoordinate = node.getLocation().x;
						configResult.node[clusterPositionInConfig].yCoordinate = node.getLocation().y;
						var xMove = node.getLocation().x - xStartToMove;
						var yMove = node.getLocation().y - yStartToMove;
						var xx;
						var yy;
						for(var i = 0; i < newCluster.clusterInfo.length; i++){
							if("cluster" + newCluster.clusterInfo[i].clusterId == nodeName){
								for(var j = 0; j < newCluster.clusterInfo[i].dpList.length; j++){
									xx = sonX[j]+xMove;
									yy = sonY[j]+yMove;
									if(xx > widthOfCanvas - widthOfImage){
										xx = widthOfCanvas - widthOfImage;
									}
									if(xx < 0){
										xx = 0;
									}
									if(yy > heightOfCanvas - heightOfImage){
										yy = heightOfCanvas - heightOfImage;
									}
									if(yy < 0){
										yy = 0;
									}
									nodes[positionInNodes[j]].entity.setLocation(xx,yy);
								}
								break;
							}
						}
					})
					node.click(function(){
						clickNodeInConfig = clusterPositionInConfig;
					})
					node.dbclick(function(event){
						node.text = prompt("请输入簇名称：","")
						configResult.node[clusterPositionInConfig].nodeName = node.text;
					})
					stage.mouseout(function(){
						if(isMouseUp == false){
							//标记需要删除的cluster中数据
							for(var i = 0; i < newCluster.clusterInfo.length; i++){
								if("cluster" + newCluster.clusterInfo[i].clusterId == configResult.node[clusterPositionInConfig].nodeId){
									newCluster.clusterInfo[i].clusterId = -1;
									break;
								}
							} 
							//标记需要删除的config中数据
							configResult.node[clusterPositionInConfig].nodeId = -1;
							//移除出画布
							scene.remove(node);
							isMouseUp = true;					
						}
					})
					scene.add(node);
					return node;
				}
				function addLink(sourceNodeId,destNodeId,name){
					var link=new JTopo.Link(sourceNodeId,destNodeId);//连线对象  
					link.lineWidth = 3;//连线大小  
					link.name = name;//连线名称  
					link.zIndex = 998; 
					scene.add(link);//场景对象添加连线对象：link. 
					return link;
				}
				function isContains(str, substr) {
					return new RegExp(substr).test(str);
				}
				function start(){
					$.ajax(getTopo);
				}			
				function submitConfig(configResult){
					var submit={
							type:"POST",
							url:"saveConfig.do",
							contentType: "application/json;charset=utf-8",
							data:configResult,
							dataType:"json",
							success:function(submitData){
								alert("提交成功");
							},
							error:function(err){
								alert("提交失败 config");
							}
						}
					return submit;
				}
				function submitCluster(newCluster){
					var submit={
							type:"POST",
							url:"saveClusterVM.do",
							contentType: "application/json;charset=utf-8",
							data:newCluster,
							dataType:"json",
							success:function(submitData){
								alert("提交成功");
							},
							error:function(err){
								alert("提交失败 cluster");
							}
						}
					return submit;
				}
				function showState(currentStep,totalSteps,status,message){
					var ops = {
						content: [
							'<div class="popup">',
								'<div class="pop_title">',
									'<h4>配置中,请稍后。。。</h4>',
								'</div>',
								'<div class="inner">',
									'<p class="info" style="color:#FFF">该过程共有' + totalSteps + '步，正在配置第' + currentStep + '步，当前步骤是：' + message + '</p>',
								'</div>',
							'</div>'].join(''),
						height:'198',
						width:'460', 
						clickClose:false,
					};
					return ops;
				}
				var getConfig={
					type:"POST",
					url:"config.do",
					dataType:"json",
					success:function(configData){
						configResult = eval(configData);
						for(var i = 0; i < configResult.node.length; i++){
							if(configResult.node[i].xCoordinate != null){
								configResult.node[i].xCoordinate = configResult.node[i].xCoordinate * widthOfCanvas;
							}
							if(configResult.node[i].yCoordinate != null){
								configResult.node[i].yCoordinate = configResult.node[i].yCoordinate * heightOfCanvas;
							}
						}
						$.ajax(getCluster);
					},
					error:function(err){
						alert("error when reading config");
					}
				}
				var getCluster={
					type:"POST",
					url:"cluster.do",
					dataType:"json",
					success:function(clusterData){
						clusterResult = eval(clusterData);
						$.ajax(getTopo);
					},
					error:function(err){
						alert("error when reading cluster");
					}
				}
				var getState={
					type:"GET",
					url:"getFlag.do",
					dataType:"json",
					success:function(stateData){
						//waitToStart.close();
						if(lastPop != null){
							lastPop.close();
						}
						stateResult = eval(stateData);
						var waitToStart = new Popup(showState(stateResult.currentStep,stateResult.totalSteps,status,stateResult.message));
						waitToStart.render();
						waitToStart.show();
						lastPop = waitToStart;
						setTimeout(next, 5000);
						//next();
						function next(){
							//waitToStart.close();
							if(stateResult.currentStep != stateResult.totalSteps){
								$.ajax(getState);
							}else{
								setTimeout(closeLast, 5000);	
							} 
						}	
						function closeLast(){
							waitToStart.close();
						}
					},
					error:function(err){
						alert("error when geting state");
					}
				}	 			
				var getTopo={		//jQuery的AJAX执行的配置对象
					type:"POST",	//设置请求方式，默认为GET，
					url:"topo.do",
					dataType:"json",    //设置期望的返回格式，因服务器返回json格式，这里将数据作为json格式对待
					success:function (topoData){
						//alert("success to get topo");
						topoResult = eval(topoData);						
						nodes = [];
						var links = [];
						var linkTags = [];
						scene.clear();						
						for (var i = 0; i < topoResult.node.length; i++){//添加节点的遍历；
							var node = new Object();
							node.id = topoResult.node[i].nodeId;
							var nodePositionInConfig = -1;
							for ( var j = 0; j < configResult.node.length; j++){
								if(topoResult.node[i].nodeId == configResult.node[j].nodeId){
									nodePositionInConfig = j;
									break;
								}
							}	
							var x;
							var y;
							if(nodePositionInConfig == -1 || ( nodePositionInConfig != -1 && configResult.node[nodePositionInConfig].nodeName == null)){
								node.name = topoResult.node[i].nodeId;
							}else{
								node.name = configResult.node[nodePositionInConfig].nodeName;
							}
							if(nodePositionInConfig == -1 || ( nodePositionInConfig != -1 && configResult.node[nodePositionInConfig].xCoordinate == null)){
								x = widthOfCanvas / 4 + Math.random() * widthOfCanvas  / 2;
								if(nodePositionInConfig != -1){
									configResult.node[nodePositionInConfig].xCoordinate = x;
								}
							}else{
								x = configResult.node[nodePositionInConfig].xCoordinate;
							}
							if(nodePositionInConfig == -1 || ( nodePositionInConfig != -1 && configResult.node[nodePositionInConfig].yCoordinate == null)){
								y = heightOfCanvas / 4 + Math.random() * heightOfCanvas  / 2;
								if(nodePositionInConfig != -1){
									configResult.node[nodePositionInConfig].yCoordinate = y;
								}
							}else{
								y = configResult.node[nodePositionInConfig].yCoordinate;
							}
							if(nodePositionInConfig == -1){
								var newNode =   {
									"nodeId": topoResult.node[i].nodeId,
									"nodeName": topoResult.node[i].nodeId,
									"xCoordinate": x,
									"yCoordinate": y
								};
								nodePositionInConfig = configResult.node.length;
								configResult.node.push(newNode);
							}
							if(isContains(topoResult.node[i].nodeId,"host")){
								node.entity = add3Node(node.name,topoResult.node[i],x,y,nodePositionInConfig);
							}else{
								node.entity = add2Node(node.name,topoResult.node[i],x,y,nodePositionInConfig);
							}
							//console.log(node.id);
							nodes.push(node);	
						}												
						for (var i = 0; i < topoResult.link.length; i++){//添加链路的遍历；
							var sourceNode;
							var destNode;
							var tag = 0;
							for(var j = 0; j < linkTags.length; j++){//判断该链路是否已经存在。
								if(((linkTags[j].sourceNodeId == topoResult.link[i].source.sourceNode)&&
								(linkTags[j].destNodeId == topoResult.link[i].destination.destNode))||
								((linkTags[j].destNodeId == topoResult.link[i].source.sourceNode)&&
								(linkTags[j].sourceNodeId == topoResult.link[i].destination.destNode))){
									tag = 1;
									continue;
								}
							}
							if(tag == 1){//根据标示位tag的值判断是否跳出本次循环；
								continue;
							}
							var linkTag = new Object();
							linkTag.sourceNodeId = topoResult.link[i].source.sourceNode;
							linkTag.destNodeId = topoResult.link[i].destination.destNode;
							linkTags.push(linkTag);
							var tagSource = false;
							var tagDestination = false;
							for (var j = 0; j < nodes.length; j++){
								if(topoResult.link[i].source.sourceNode == nodes[j].id){
									sourceNode = nodes[j].entity;
									tagSource = true;
									break;
								}
							}
							for (var j = 0; j < nodes.length; j++){
								if(topoResult.link[i].destination.destNode == nodes[j].id){
									destNode = nodes[j].entity;
									tagDestination = true;
									break;
								}
							}	
							if(tagSource && tagDestination){
								var link = new Object();
								link.name = topoResult.link[i].linkId;
								link.id = addLink(sourceNode,destNode,topoResult.link[i].linkId);
								links.push(link);	
							}
						}			
						/* $("#newClusterName").bind("input propertychange", function() {//右侧栏中，给name绑定动作；
							newCluster.name = $(this).val();	
						}); */
						$("#add").click(function(){
							if(isNew){
							//在前端添加一个分簇的图形；
								var x = Math.random() *(widthOfCanvas - widthOfImage *(2 * 1 + 1));
								var y = Math.random() *(heightOfCanvas - heightOfImage *(2 * 1 + 1));
								clusterNum++;
								var nodeName = "cluster" + clusterNum;
								//修改cluster数据；
								var newClusterNode = new Object();
								newClusterNode.clusterId = newCluster.clusterInfo.length + 1;
								newClusterNode.dpList = new Array();
								newClusterNode.edgePort = new Array();
								newCluster.clusterInfo.push(newClusterNode);
								//alert($.toJSON(newCluster));
								//修改config数据；
								var newNode =   {
									"nodeId": "cluster" + clusterNum,
									"nodeName": "cluster" + clusterNum,
									"xCoordinate": x,
									"yCoordinate": y,
									"width":widthOfImage *(2 * 2 + 1),
									"height":heightOfImage *(2 * 2 + 1)
								};
								var clusterPositionInConfig = configResult.node.length;
								configResult.node.push(newNode);
								
								//alert($.toJSON(newNode));
								//alert($.toJSON(configResult.node[configResult.node.length-1]));
								//alert(clusterNum);
								//alert($.toJSON(newNode));
								
								var clusterNode = new Object();
								clusterNode.id = "cluster" + clusterNum;
								clusterNode.x = x;
								clusterNode.y = y;
								clusterNode.entity = addCluster(nodeName,x,y,clusterPositionInConfig);
								nodes.push(clusterNode);
								//alert($.toJSON(configResult.node));
							}else{
								alert("交换机已启动，请”一键还原“再修改分簇信息重新启动！");
							}
						});
						$("#recovery").click(function(){
							for(var i = 0; i < nodes.length; i++){
								//console.log(nodes[i].id);
								if(isContains(nodes[i].id,"cluster")){
									scene.remove(nodes[i].entity);
									//i--;
								}
							}
							nodes.splice(nodes.length - clusterNum ,clusterNum);
							configResult.node.splice(configResult.node.length - clusterNum ,clusterNum);
							isNew = true;
							clusterNum = 0;
							newCluster.clusterInfo=[];
							/* for(var i = 0; i < nodes.length; i++){
								console.log(nodes[i].id);
							}
							console.log("config");
							for(var i = 0; i < configResult.node.length; i++){
								console.log(configResult.node[i].nodeId);
							} */
						});
						$("#submit").click(function(){
							//数据提交前处理----去除id为-1的删除项；
							if(isNew){
								for(var i = 0; i < configResult.node.length; i++){//去除config中的多余项：id为-1的
									if(configResult.node[i].nodeId == -1){
										configResult.node.splice(i,1);
										i--;
										continue;
									}
									if(configResult.node[i].xCoordinate != null){
										var xRatio = configResult.node[i].xCoordinate / widthOfCanvas;
										xRatio = xRatio.toFixed(3);
										configResult.node[i].xCoordinate = xRatio;
										//alert(configResult.node[i].xCoordinate);
									}
									if(configResult.node[i].yCoordinate != null){
										var yRatio = configResult.node[i].yCoordinate / heightOfCanvas;
										yRatio = yRatio.toFixed(3);
										configResult.node[i].yCoordinate = yRatio;
									}
								}
								for(var i = 0; i < newCluster.clusterInfo.length; i++){//去除newcluster中的多余项：id为-1的和空簇；
									if( newCluster.clusterInfo[i].clusterId == -1){
										newCluster.clusterInfo.splice(i,1);
										i--;//前一步在删除数组，--i保持
										continue;
									}
									if(newCluster.clusterInfo[i].dpList.length == 0){
										newCluster.clusterInfo.splice(i,1);
										i--;
									}
								}
							//数据提交前处理----添加cluster中的edgePort；
								for(var i = 0; i < newCluster.clusterInfo.length; i++){//给第i个cluster添加port；
									for(var j = 0; j < topoResult.link.length; j++){//遍历所有链路；
										var savePort = null;
										var flag = 0;
										for(var k = 0; k < newCluster.clusterInfo[i].dpList.length; k++){
											if(topoResult.link[j].source.sourceNode == newCluster.clusterInfo[i].dpList[k]){
												savePort = topoResult.link[j].source.sourceTp;
												flag++;
												break;
											}
										}
										for(var k = 0; k < newCluster.clusterInfo[i].dpList.length; k++){
											if(topoResult.link[j].destination.destNode == newCluster.clusterInfo[i].dpList[k]){
												savePort = topoResult.link[j].destination.destTp;
												flag--;
												break;
											}
										}
										if(flag != 0){
						   
											var edgePortUseToAdd = new Object();
											var idAndPort = savePort.split(":");
											var flagInFindDpId = false;
											var flagInFindDpPort = false;
						//alert(j);
						//alert("分割之前" + savePort);
											edgePortUseToAdd.dpId = idAndPort[0] + ":" + idAndPort[1];
											edgePortUseToAdd.dpPort = new Array(idAndPort[2]);
						//alert("dpid" + edgePortUseToAdd.dpId);
						//alert("dpport" + edgePortUseToAdd.dpPort);
											for(var k = 0; k < clusterResult.clusterInfo.length; k++){
												if(clusterResult.clusterInfo[k].dpList[0] == edgePortUseToAdd.dpId){
													//var positionInFirstCluster = -1;
													//if()
													edgePortUseToAdd.macAddress = new Array(clusterResult.clusterInfo[k].edgePort[0].macAddress[$.inArray(edgePortUseToAdd.dpPort+"",clusterResult.clusterInfo[k].edgePort[0].dpPort)]);
													edgePortUseToAdd.ipAddress = new Array(clusterResult.clusterInfo[k].edgePort[0].ipAddress[$.inArray(edgePortUseToAdd.dpPort+"",clusterResult.clusterInfo[k].edgePort[0].dpPort)]);
													edgePortUseToAdd.network = new Array(clusterResult.clusterInfo[k].edgePort[0].network[$.inArray(edgePortUseToAdd.dpPort+"",clusterResult.clusterInfo[k].edgePort[0].dpPort)]);
												}
											}
											
											if(newCluster.clusterInfo[i].edgePort.length == 0){//如果edgePort为空则直接写入；
												newCluster.clusterInfo[i].edgePort.push(edgePortUseToAdd);
											} else{
												for(var k = 0; k < newCluster.clusterInfo[i].edgePort.length; k++){//遍历edgeport判断是否有相同dpid
													if(newCluster.clusterInfo[i].edgePort[k].dpId == edgePortUseToAdd.dpId){//如果找到相同的dpid
														flagInFindDpId = true;
														for(var d = 0; d < newCluster.clusterInfo[i].edgePort[k].dpPort.length; d++){//遍历dpid中的dpport；
															if(newCluster.clusterInfo[i].edgePort[k].dpPort[d] == edgePortUseToAdd.dpPort){//dpPort相同不用处理；
																flagInFindDpPort = true;
															}
														}
														 if(flagInFindDpPort == false){//所有的dpport都不同，则部分添加；
																newCluster.clusterInfo[i].edgePort[k].dpPort.push(edgePortUseToAdd.dpPort[0]);
																newCluster.clusterInfo[i].edgePort[k].macAddress.push(edgePortUseToAdd.macAddress[0]);
																newCluster.clusterInfo[i].edgePort[k].ipAddress.push(edgePortUseToAdd.ipAddress[0]);
																newCluster.clusterInfo[i].edgePort[k].network.push(edgePortUseToAdd.network[0]);
														}
													}
												}
												if(flagInFindDpId == false){//如果没有相同dpid 直接添加
													newCluster.clusterInfo[i].edgePort.push(edgePortUseToAdd);
												}
											} 

										}
									}
								}
							//数据提交前处理----修改两个表中的timestamp
								newCluster.timeStamp = configResult.timeStamp = (new Date()).valueOf(); 
								newCluster.name = null;
							//数据提交；

								clusterBack = $.toJSON(newCluster);
								configBack = $.toJSON(configResult);
								$.ajax(submitCluster(clusterBack));
								$.ajax(submitConfig(configBack)); 
								isNew = false;
								$.ajax(getState);
							}else{
								alert("交换机已启动，请勿重复提交！");
							}
						});				
					},		 
					error:function(err){
						alert("error when reading topo");
					}
				}
				$.ajax(getConfig);
			});	