$(document).ready(function() {
	var canvas = document.getElementById("canvas");
	var stage = new JTopo.Stage(canvas);
	stage.mousedown(function(){isDrag = true;isChangeByUser = true;effect.stop();})
	stage.mouseup(function(){isDrag = false;})
	stage.mousedrag(function(){isDrag = true;})
	var scene = new JTopo.Scene();
	stage.add(scene);
	var widthOfCanvas = $(document).width()- 325 -17;
	var heightOfCanvas = $(window).height()-2;
	var widthOfImage = Math.sqrt(widthOfCanvas*heightOfCanvas/840000)*65;
	var heightOfImage = Math.sqrt(widthOfCanvas*heightOfCanvas/840000)*50;
	$("#canvas").attr('width', widthOfCanvas);
	$("#canvas").attr('height', heightOfCanvas);
	var configResult =null;
	var historyTopoResult = null;
	var clickNodeInConfig = -1;//use to record 
	var isDrag = false;
	var isHaveConfig = false;
	var isChangeByUser = false;
	var nodes = [];
	var effect;
	function springEffect(tmp){
		return JTopo.Effect.spring({
			minLength: Math.sqrt(widthOfCanvas*heightOfCanvas)/3,// 节点之间最短距离
			spring:tmp
		});
	}	
	/**
	*@function:Use to add node on canvas;
	*@param:nodeName:Use to show the node's name on canvas;
	*		type:Used to distinguish different node types;
	*		x/y:Used to specify the location of a node;
	*@return:Object,Handle of the node; 		
	*/
	function addNodes(nodeName, nodeId, type, x, y, nodePositionInConfig) {
		var node = new JTopo.Node(nodeName);
		node.setSize(widthOfImage, heightOfImage);
		node.setLocation(x, y);
		node.fontColor = "0,0,0";
		node.mousedrag(function() {
			showInfoInSidebar(node);//show the information of the node in the sidebar,include the name of node and the position of it;
			if(nodePositionInConfig != -1){//change the datas in configResult(use to record the information of node config);
				configResult.node[nodePositionInConfig].xCoordinate = node.getLocation().x/widthOfCanvas;
				configResult.node[nodePositionInConfig].xCoordinate = configResult.node[nodePositionInConfig].xCoordinate.toFixed(3);
				//alert(configResult.node[nodePositionInConfig].xCoordinate);
				configResult.node[nodePositionInConfig].yCoordinate = node.getLocation().y/heightOfCanvas;
				configResult.node[nodePositionInConfig].yCoordinate = configResult.node[nodePositionInConfig].yCoordinate.toFixed(3);
			}
		})
		node.click(function() {
			showInfoInSidebar(node);
			if(nodePositionInConfig != -1){
				clickNodeInConfig = nodePositionInConfig;
			}
		}) 
		node.dbclick(function() {
			window.open("flowtable.html?id="+nodeId);
		}) 		
		if(type == "switch"){
			node.setImage("images/switchNew.png", false);
		}
		if(type == "host"){
			node.setImage("images/host.png", false);
		}
		scene.add(node);
		return node;
	}
	/**
	*@function:Use to show Information of the node on sidebar which is on the right part on the page;
	*@param:node:Use to tell the function which node's information to show, is an Object;
	*@return:nothing; 
	*/
	function showInfoInSidebar(node){
		$("#name").val(node.text);
		$("#x_coordinate").val(parseInt(node.getLocation().x));
		$("#y_coordinate").val(parseInt(node.getLocation().y));
	}
	/**
	*@function:Use to add link between two nodes;
	*@param:firstNode/secondNode:Object, nodes on canvas which return by function addNodes;
	*@return:Object,Handle of the link; 
	*/
	function addLink(firstNode, secondNode) {
		var link = new JTopo.Link(firstNode, secondNode); //连线对象  
		link.lineWidth = 3; //连线大小  
		scene.add(link); //场景对象添加连线对象：link. 
		return link;
	}
	/**
	*@function:Use to judge if the partString is contained by the fullString;
	*@param:partString:A fragment of string;
	*		fullString:A long string which might contained the partString;
	*@return:Boolean; 
	*/
	function isContains(fullString, partString) {
		return new RegExp(partString).test(fullString);
	}
	/**
	*@function:Use to refresh the page if there is no operate on the page;
	*		if there has some operate on the canvas, these function will help to stop refresh the page and wait;
	*		if there has none operate on the canvas, 
	*@param:time:wait time;
	*@return: nothing;
	*/
	function wait(time) {
		setTimeout(function(){
			if (isDrag == true) {
				setTimeout(wait, 0);
			}else {
				setTimeout(function(){useAjax("topo.do",null,afterRequestTopo)}, 0);
			}
		}, time);
        
	}
	/**
	*@function:Use to submit datas to appointed url and get the datas from sever;
	*@param:url:the url of servers to submit or get the datas;
	*		submitData:the data is string type which accord with json using to submit to server;
			next:function,which only start when this ajax success;
	*@return:nothing; 
	*/
	function useAjax(url,submitData,next) {
		var submit = {
			type: "POST",
			url: url,
			contentType: "application/json;charset=utf-8",
			data: submitData,
			dataType: "json",
			success: function(data) {
				next(eval(data));
			},
			error: function(err) {
				alert("提交失败");
			}
		}
		$.ajax(submit);
	}
	/**
	*@function:the success callback function after get config using ajax;
	*@param:config:the data which get by ajax request of get-config;
	*@return:nothing; 
	*/
	function afterRequestConfig(config){
		configResult = config; 
		useAjax("topo.do",null,afterRequestTopo);
	}
	/**
	*@function:the success callback function after get topo using ajax;
	*@param:topoResult:the data which get by ajax request of get-topo;
	*@return:nothing; 
	*/
	function afterRequestTopo(topoResult){
		if(isDrag == false && $.toJSON(topoResult) != $.toJSON(historyTopoResult)){
			historyTopoResult = topoResult;
			nodes = [];
			var links = [];
			var linkTags = [];
			scene.clear();
			if(topoResult.node.length < 10){
				widthOfImage = Math.sqrt(widthOfCanvas*heightOfCanvas/840000)*65*1.1;
				heightOfImage = Math.sqrt(widthOfCanvas*heightOfCanvas/840000)*50*1.1; 
				effect = springEffect(0.03);
			}else if(topoResult.node.length < 20){
				widthOfImage = Math.sqrt(widthOfCanvas*heightOfCanvas/840000)*65*1;
				heightOfImage = Math.sqrt(widthOfCanvas*heightOfCanvas/840000)*50*1; 
				effect = springEffect(0.02);
			}else{
				widthOfImage = Math.sqrt(widthOfCanvas*heightOfCanvas/840000)*65*0.9;
				heightOfImage = Math.sqrt(widthOfCanvas*heightOfCanvas/840000)*50*0.9; 
				effect = springEffect(0.003);
			}
			for(var i = 0; i < topoResult.node.length; i++) { //添加节点的遍历；
				var node = new Object();
				node.id = topoResult.node[i].nodeId;
				var nodePositionInConfig = -1;
				for (var j = 0; j < configResult.node.length; j++) {
					if (topoResult.node[i].nodeId == configResult.node[j].nodeId) {
						nodePositionInConfig = j;
						break;
					}
				}
				var x;
				var y;
				if (nodePositionInConfig == -1 || (nodePositionInConfig != -1 && configResult.node[nodePositionInConfig].nodeName == null)) {
					node.name = topoResult.node[i].nodeId;
				} else {
					node.name = configResult.node[nodePositionInConfig].nodeName;
				}
				if (nodePositionInConfig == -1 || (nodePositionInConfig != -1 && configResult.node[nodePositionInConfig].xCoordinate == null)) {
					x = Math.ceil(widthOfCanvas/4 + Math.random() * (widthOfCanvas/2));
					if (nodePositionInConfig != -1) {
						configResult.node[nodePositionInConfig].xCoordinate = x / widthOfCanvas;
					}
				} else {
					x = configResult.node[nodePositionInConfig].xCoordinate * widthOfCanvas;
					isHaveConfig = true;
				}
				if (nodePositionInConfig == -1 || (nodePositionInConfig != -1 && configResult.node[nodePositionInConfig].yCoordinate == null)) {
					y = Math.ceil(heightOfCanvas/4 + Math.random() * (heightOfCanvas/2));
					if (nodePositionInConfig != -1) {
						configResult.node[nodePositionInConfig].yCoordinate = y / heightOfCanvas;
					}
				} else {
					y = configResult.node[nodePositionInConfig].yCoordinate * heightOfCanvas;
					isHaveConfig = true;
				}
				if (nodePositionInConfig == -1) {
					var newNode = {
						"nodeId": topoResult.node[i].nodeId,
						"nodeName": null,
						"xCoordinate": x / widthOfCanvas,
						"yCoordinate": y / heightOfCanvas
					};
					nodePositionInConfig = configResult.node.length;
					configResult.node.push(newNode);
				}
				if (isContains(topoResult.node[i].nodeId, "host")) {
					node.entity = addNodes(node.name, node.id, "host", x, y, nodePositionInConfig);
				} else {
					node.entity = addNodes(node.name, node.id, "switch", x, y, nodePositionInConfig);
				}
				
				nodes.push(node);
			}
			for(var i = 0; i < topoResult.link.length; i++) { //添加链路的遍历；
				var sourceNode;
				var destNode;
				var tag = 0;
				for (var j = 0; j < linkTags.length; j++) { //判断该链路是否已经存在。
					if (((linkTags[j].sourceNodeId == topoResult.link[i].source.sourceNode) && (linkTags[j].destNodeId == topoResult.link[i].destination.destNode)) || ((linkTags[j].destNodeId == topoResult.link[i].source.sourceNode) && (linkTags[j].sourceNodeId == topoResult.link[i].destination.destNode))) {
						tag = 1;
						continue;
					}
				}
				if (tag == 1) { //根据标示位tag的值判断是否跳出本次循环；
					continue;
				}
				var linkTag = new Object();
				linkTag.sourceNodeId = topoResult.link[i].source.sourceNode;
				linkTag.destNodeId = topoResult.link[i].destination.destNode;
				linkTags.push(linkTag);
				var tagSource = false;
				var tagDestination = false;
				for (var j = 0; j < nodes.length; j++) {
					if (topoResult.link[i].source.sourceNode == nodes[j].id) {
						sourceNode = nodes[j].entity;
						tagSource = true;
						break;
					}
				}
				for (var j = 0; j < nodes.length; j++) {
					if (topoResult.link[i].destination.destNode == nodes[j].id) {
						destNode = nodes[j].entity;
						tagDestination = true;
						break;
					}
				}
				if (tagSource && tagDestination) {
					var link = new Object();
					link.name = topoResult.link[i].linkId;
					link.id = addLink(sourceNode, destNode);
					links.push(link);
				}
			}    
				
		}
		if(isChangeByUser == false && isHaveConfig == false){
			for(var i = 0; i < nodes.length; i++){
				for(var j = i + 1; j < nodes.length; j++){
					effect.addNode(nodes[i].entity, nodes[j].entity);
				}
			}
			effect.play();	
		}
		wait(3000);
	}
	$("#name").bind("input propertychange",function() {
		if (clickNodeInConfig != -1) {
			configResult.node[clickNodeInConfig].nodeName = $(this).val();
			for (var i = 0; i < nodes.length; i++) {
				if (nodes[i].id == configResult.node[clickNodeInConfig].nodeId) {
					nodes[i].entity.text = $(this).val();
					break;
				}
			}
		}
	});
	$("#x_coordinate").bind("input propertychange",function() {
		if (clickNodeInConfig != -1) {
			configResult.node[clickNodeInConfig].xCoordinate = parseInt($(this).val())/widthOfCanvas;
			for (var i = 0; i < nodes.length; i++) {
				if (nodes[i].id == configResult.node[clickNodeInConfig].nodeId) {
					nodes[i].entity.x = parseInt($(this).val());
					break;
				}
			}
		}
	});
	$("#y_coordinate").bind("input propertychange",function() {
		if (clickNodeInConfig != -1) {
			configResult.node[clickNodeInConfig].yCoordinate = parseInt($(this).val())/heightOfCanvas;
			for (var i = 0; i < nodes.length; i++) {
				if (nodes[i].id == configResult.node[clickNodeInConfig].nodeId) {
					nodes[i].entity.y = parseInt($(this).val());
					break;
				}
			}
		}
	});
	$("#submit").click(function() {
		console.log($.toJSON(configResult));
		console.log(widthOfCanvas);
		console.log(heightOfCanvas); 
		var maxx = 1; 
		var minx = 0;
		var maxy = 1;
		var miny = 0;
		for(var i = 0; i < configResult.node.length; i++){
			for(var j = 0; j < nodes.length; j++){
				if(configResult.node[i].nodeId == nodes[j].id){
					configResult.node[i].xCoordinate = nodes[j].entity.getLocation().x/widthOfCanvas;
					configResult.node[i].yCoordinate = nodes[j].entity.getLocation().y/heightOfCanvas;
					configResult.node[i].xCoordinate = configResult.node[i].xCoordinate.toFixed(3);
					configResult.node[i].yCoordinate = configResult.node[i].yCoordinate.toFixed(3);
					break;
				}
			}
			if(configResult.node[i].xCoordinate > maxx){
				maxx = configResult.node[i].xCoordinate;
			}
			if(configResult.node[i].xCoordinate < minx){
				minx = configResult.node[i].xCoordinate;
			}
			if(configResult.node[i].yCoordinate > maxy){
				maxy = configResult.node[i].yCoordinate;
			}
			if(configResult.node[i].yCoordinate < miny){
				miny = configResult.node[i].yCoordinate;
			} 
		}
		if(maxx > 1){
			for(var i = 0; i < configResult.node.length; i++){
				configResult.node[i].xCoordinate -= (maxx - 1); 
			}
		}
		if(minx < 0){
			for(var i = 0; i < configResult.node.length; i++){
				configResult.node[i].xCoordinate += (0 - minx); 
			}
		}
		if(maxy > 1){
			for(var i = 0; i < configResult.node.length; i++){
				configResult.node[i].yCoordinate -= (maxy - 1); 
			}
		}
		if(miny < 0){
			for(var i = 0; i < configResult.node.length; i++){
				configResult.node[i].yCoordinate += (0 - miny); 
			}
		}
		
		console.log($.toJSON(configResult));
		useAjax("saveConfig.do",$.toJSON(configResult),null); 
		
	});
	//start of program;
	useAjax("config.do",null,afterRequestConfig);
});