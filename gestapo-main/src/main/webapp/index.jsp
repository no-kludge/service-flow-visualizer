<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html">
<html>
<head>
<title>Service Flow Visualizer</title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.css">
	<script type="text/javascript" src="https://code.jquery.com/jquery-2.2.1.min.js"></script>
	<script type="text/javascript" src="https://code.jquery.com/ui/1.11.4/jquery-ui.min.js"></script>
	<script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/fabric.js/1.5.0/fabric.min.js"></script>
	<script>
		
		// Global Variables and Data Structures
		var canvas;
		
		var invocation = function(seqNo, fromServiceName, fromClassName, toServiceName, toClassName, methSig) {
			this.seqNo = seqNo;
			this.fromServiceName = fromServiceName;
			this.fromClassName = fromClassName;
			this.toServiceName = toServiceName;
			this.toClassName = toClassName;
			this.methSig = methSig;
		}
		
		var serviceClassPair = function(serviceName, className) {
			this.serviceName = serviceName;
			this.className = className;
		}
		
		var blockStruct = function(rect, verLine) {
			this.rect = rect;
			this.verLine = verLine;
		}
		
		var blockStructArr = [];
		
		$( document ).ready(
				function() {
		    		$( "button" ).button().click(function( event ) {
		    			event.preventDefault();
		    		});
		    		
		    		$("#draw").click(draw);
		    		$("#clear").click(clear);
		    
		});
		
		var clear = function() {
			canvas.clear();
		}
		
		var draw = function() {
			$.ajax({
				url : "/ServiceFlowVisualizer/getData"
			}).done(function(data) {
				canvas = new fabric.Canvas('c');
			    canvas.backgroundColor="#999966";
			    
			    var serviceClassPairArr = data[0];
			    var invocationArr = data[1];
			    
				// draw swim lanes
			    var swimLaneLeft = 150;
			    serviceClassPairArr.forEach(function(item) {
			    	swimLaneId = item.serviceName + ":" + item.className;
			    	blockStructArr[swimLaneId] = drawSwimLane(item.serviceName, item.className, swimLaneLeft);
			    	swimLaneLeft = swimLaneLeft + 400;
			    }); 
			    
			    // draw invocations
			    var numItr = 1;
			    invocationArr.forEach(function(item) {
			    	fromSwimLaneId = item.fromServiceName + ":" + item.fromClassName;
			    	toSwimLaneId = item.toServiceName + ":" + item.toClassName;
			    	drawInvocationLine(fromSwimLaneId, toSwimLaneId, item.methSig, numItr++);
			    });
			});
		}
		
		var drawSwimLane = function(serviceName, className, swimLaneLeft) {
			// create a rectangle object
		    var rect = new fabric.Rect({
		      fill: 'white',
		      width: 200,
		      height: 100,
		      originX: 'center',
	    	  originY: 'center'
		    });
		    
		    var serviceText = new fabric.Text(serviceName, {
		    	  fontSize: 20,
		    	  originX: 'center',
		    	  originY: 'center'
		    	});
		    
		    var classText = new fabric.Text(className, {
		    	  fontSize: 20,
		    	  originX: 'center',
		    	  originY: 'top'
		    	});
		    
		 	// group rectangle and text objects
		    var recTexGrp = new fabric.Group([rect, serviceText, classText], {
		    	  left: swimLaneLeft,
		    	  top: 100
		    	});
		    
		 	// add rect text group to canvas
		    canvas.add(recTexGrp);
		    
		 	// create vertical line object
		    var verLine = new fabric.Line();
		    verLine.left = recTexGrp.left + (recTexGrp.width/2);
		    verLine.top = recTexGrp.top + (recTexGrp.height);
		    verLine.height = 400;
		    verLine.stroke = 'white';
		    
		 	// add line to canvas
		    canvas.add(verLine);
		 	
		 	return new blockStruct(rect, verLine);
		 	
		}
		
		function drawInvocationLine(fromSwimLaneId, toSwimLaneId, methSig, numItr, isLast) {
		    var fromVerLine = blockStructArr[fromSwimLaneId].verLine;
		    var toVerLine = blockStructArr[toSwimLaneId].verLine;
		    
			// create horizontal line object
		    var horLine = new fabric.Line();
		    horLine.originX = 'center';
		    horLine.originY = 'center';
		    horLine.width = toVerLine.left - fromVerLine.left;
		    horLine.stroke = 'white';
		    
		    var horText = new fabric.Text(methSig, {
		    	  fontSize: 20,
		    	  originX: 'center',
		    	  originY: 'bottom'
		    	});
		    
		    
		 	// group line and text objects
		    var linTexGrp = new fabric.Group([horLine, horText], {
		    	  left: fromVerLine.left,
		    	  top: fromVerLine.top + (fromVerLine.height * (numItr/10))
		    	});
		    
		 	// add invocation line to canvas
		 	canvas.add(linTexGrp);
		}
	</script>
</head>
<body>
	<h2 style="padding: 20px;text-align: center;font-family: Comic-Sans;font-size: 45px;">Service Flow Visualizer</h2>
	<div>
		<div style="float: left;padding-left: 32px;width: 10%;"><button id="draw">Draw</button></div>
		<div style="float:left;"><button id="clear">Clear</button></div>
	</div>
	<div style="width: 98%; overflow: scroll; height: 100%;padding:30px;"><canvas id="c" width="3200" height="1000"></canvas></div>
</body>
</html>