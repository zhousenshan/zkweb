<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<jsp:include page="head.jsp"></jsp:include>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>This is ZkWeb</title>
<script type="text/javascript">

$(function(){
	
	initTree();
	
});

function initTree(){
	
	$('#zkTree').tree({
		checkbox: false,
		url: "zk/queryZnode",
		animate:true,
		lines:true,
		onContextMenu: function(e,node){  
            e.preventDefault();  
            $(this).tree('select',node.target);  
            $('#mm').menu('show',{  
                left: e.pageX,  
                top: e.pageY  
            });  
        },
		onClick:function(node){
			var tab = $('#zkTab').tabs('getSelected');
			//var index = $('#zkTab').tabs('getTabIndex',tab);
			//alert(index);
			if(tab != null){
				tab.title=node.text;
				//tab.panel('refresh', "zk/queryZnodeInfo?path="+node.attributes.path);
				$('#zkTab').tabs('update', {
					tab: tab,
					options: {
						title: node.text,
						href: "zk/queryZnodeInfo?path="+encodeURI(encodeURI(node.attributes.path))  
					}
				});
			}else {
				$('#zkTab').tabs('add',{
					id:0,
		            title:node.text,  
		            href: "zk/queryZnodeInfo?path="+encodeURI(encodeURI(node.attributes.path)) 
		            //closable:true  
	        	}); 
			}
			
		},
		onBeforeExpand:function(node,param){
			if(node.attributes != null){
				$('#zkTree').tree('options').url = "zk/queryZnode?path="+encodeURI(encodeURI(node.attributes.path)); 
			}
		}
	});
	
}


function remove(){ 
	$.extend($.messager.defaults,{  
		ok:"确定",  
		cancel:"取消"  
	});
	
	 var node = $('#zkTree').tree('getSelected');  
     if (node){  
     	if('/'==node.attributes.path || '/zookeeper'==node.attributes.path || '/zookeeper/quota'==node.attributes.path){
     		$.messager.alert('提示','不能删除此节点！');
     		return;
     	}
     
     	$.messager.confirm('提示', '删除'+node.attributes.path+'下所有子节点！确认吗？', function(r){  
    	    if (r){  
                //var s = node.text;  
                if (node.attributes){  
                	 _path = node.attributes.path ;
                	 $.post("zk/deleteNode", {path: _path},
               				function(data){
               					//alert("Data Loaded: " + data);
               					$.messager.alert('提示', data);
               					initTree();
               					//var tab = $('#zkTab').tabs('getTab',0);
               					//alert(tab.title);
               					$('#zkTab').tabs('close',0);
               				}
                	);
                }  
    	    }  
        }); 
     }else {
     	$.messager.alert('提示', '请选择一个节点');
     };
     
	 
}  

function collapse(){  
    var node = $('#zkTree').tree('getSelected');  
    $('#zkTree').tree('collapse',node.target);  
}  

function expand(){  
    var node = $('#zkTree').tree('getSelected');  
    $('#zkTree').tree('expand',node.target);  
}

function addzkNode(){
	var _path = "/";
	var node = $('#zkTree').tree('getSelected');  
    if (node){  
        //var s = node.text;  
        if (node.attributes){  
        	 _path = node.attributes.path ;
        }  
    };
    _nodeName = $('#zkNodeName').val();
    //alert($('#zkNodeName').val());
    
	$.post("zk/createNode", { nodeName: _nodeName, path: _path},
			function(data){
				//alert("Data Loaded: " + data);
				$.messager.alert('提示', data);
				$('#w').window('close');
				initTree();
			}
	);
}

</script>

</head>


<body class="easyui-layout">  
<!--  
    <div data-options="region:'north',border:false" style="height:60px;background:#B3DFDA;padding:10px">ZkWeb</div> 
   
    
    <div data-options="region:'east',split:true,collapsed:true,title:'East'" style="width:100px;padding:10px;">east region</div>  
    <div data-options="region:'south',border:false" style="height:50px;background:#A9FACD;padding:10px;">south region</div>  
     --> 
    <div data-options="region:'west',split:true,title:'目录'" style="width:150px;padding:10px;height:120px;">
    	<ul id="zkTree" class="easyui-tree" ></ul>
    	<!-- right -->
    	<div id="mm" class="easyui-menu" style="width:120px;">  
	        <div onclick="javascript:$('#w').window('open');" data-options="iconCls:'icon-add'">添加</div>  
	        <div onclick="remove()" data-options="iconCls:'icon-remove'">删除</div>  
	        <div class="menu-sep"></div>  
	        <div onclick="expand()">展开</div>  
	        <div onclick="collapse()">收起</div>  
        </div>
    </div> 
    
    <div data-options="region:'center'" border="false" style="overflow: hidden;">  
	    <div class="easyui-tabs" id="zkTab" data-options="tools:'#tab-tools',toolPosition:'left'">  
		    <div title="Home" style="padding:10px;">  
		        Welcome! 
		    </div>  
		</div>  
		<div id="tab-tools">  
        	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'" onclick="javascript:$('#w').window('open');"></a>  
        	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove'" onclick="remove()"></a>  
    	</div>
		
    </div>  
    
    <!-- add -->
    <div id="w" class="easyui-window" title="添加节点" data-options="iconCls:'icon-add',modal:true,closed:true" style="width:500px;padding:10px;">  
        
        <div style="text-align:center;padding:5px">
        	输入节点名称:
       		<input id="zkNodeName" class="easyui-validatebox" type="text" data-options="required:true,tipPosition:'right'"></input> 
        </div>
        
        <div style="text-align:center;padding:5px">
        	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="addzkNode()">保存</a>
        	<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="$('#w').window('close');" >取消</a>  
        </div>
        
    </div>  
    
</body> 

</html>