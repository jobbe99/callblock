// JavaScript Document
var tempLast='\
	<ul class="app-list tx-blue">\
	<label>Last Incoming Calls</label>\
	{{#data}}\
		<li class="call">\
			<div class="call-block d{{numid}} {{? block==1 }} block_back {{/?}}">\
				<div class="call-c0 block_point_{{block}} p{{numid}}" id="p{{numid}}">\
					<i class="fa fa-circle"></i> \
				</div>\
				<div class="call-c1">\
					<span class="name">{{name}}</span>\
					<span class="num">{{num}}</span>\
					<span class="date">{{date}}</span>\
				</div>\
				<div class="call-c2">\
					<div class="app-button block_{{block}} b{{numid}}">\
						<a href="#" onclick="manage(\'{{num}}\', {{block}} );">\
						<span class="block_text t{{numid}}">\
						{{? block == 1 }}\
							accept\
						{{^?}}\
							block\
						{{/?}}\
						</span>\
						</a>\
					</div>\
				</div>\
			</div>\
		</li>\
	{{/data}}\
	</ul>';
	
// JavaScript Document
var tempInOut='\
	<ul class="app-list tx-blue">\
	<label>Last Incoming Call</label>\
	{{#inout}}\
		<li class="call">\
			<div class="call-block">\
				<div class="call-c0">\
					{{? block == 1 }}\
						<i class="fa fa-exclamation-circle tx-red"></i> \
					{{^?}}\
						<i class="fa fa-circle tx-green"></i> \
					{{/?}}\
				</div>\
				<div class="call-c1">\
					<h1>{{num}}</h1>\
					<h2>{{t}}</h2>\
				</div>\
				<div class="call-c2">\
					<div class="app-button">\
						<a href="#" onclick="manage(\'{{num}}\', {{block}} );">\
						{{? block == 1 }}\
							<i class="fa fa-check-circle tx-green"></i> accept\
						{{^?}}\
							<i class="fa fa-check-circle tx-red"></i> block\
						{{/?}}\
						</a>\
					</div>\
				</div>\
			</div>\
		</li>\
	{{/inout}}\
	</ul>';

var tempBlock='\
<ul class="app-list tx-blue">\
	<label>Last Incoming Calls</label>\
	{{#inout}}\
		<li class="call">\
			<div class="call-block d{{numid}} {{? block==1 }} block_back {{/?}}">\
				<div class="call-c0 block_point_{{block}} p{{numid}}" id="bb{{numid}}">\
					<i class="fa fa-circle"></i> \
				</div>\
				<div class="call-c1">\
					<span class="name">{{num}}</span>\
					<span class="date">{{t}}</span>\
				</div>\
				<div class="call-c2">\
					<div class="app-button block_{{block}} b{{numid}}">\
						<a href="#" onclick="manage(\'{{num}}\', {{block}} );">\
						<span class="block_text t{{numid}}">\
							accept\
						</span>\
						</a>\
					</div>\
				</div>\
			</div>\
		</li>\
	{{/inout}}\
	</ul>';
	
	
// JavaScript Document
var tempMyBlock='\
	<ul class="app-list tx-blue">\
	<label>My blocked number</label>\
	{{#inout}}\
		<li class="call">\
			<div class="call-block d{{numid}} block_back">\
				<div class="call-c0 block_point_1 p{{numid}}" id="mb{{numid}}">\
					<i class="fa fa-circle"></i> \
				</div>\
				<div class="call-c1">\
					<span class="num">{{num}}</span>\
					<span class="nome">{{numid}}</span>\
				</div>\
				<div class="call-c2">\
					<div class="app-button block_1 b{{numid}}">\
						<a href="#" onclick="manage(\'{{num}}\', 1 );">\
						<span class="block_text t{{numid}}">\
							accept\
						</span>\
						</a>\
					</div>\
				</div>\
			</div>\
		</li>\
	{{/inout}}\
	</ul>';

// JavaScript Document
var tempMyWhite='\
	<ul class="app-list tx-blue">\
	<label>My white list</label>\
	{{#inout}}\
		<li class="call">\
			<div class="call-block d{{numid}} block_back">\
				<div class="call-c0 block_point_1 p{{numid}}" id="mw{{numid}}">\
					<i class="fa fa-circle"></i> \
				</div>\
				<div class="call-c1">\
					<span class="num">{{num}}</span>\
					<span class="nome">{{numid}}</span>\
				</div>\
				<div class="call-c2">\
					<div class="app-button block_1 b{{numid}}">\
						<a href="#" onclick="manage(\'{{num}}\', 0 );">\
						<span class="block_text t{{numid}}">\
							accept\
						</span>\
						</a>\
					</div>\
				</div>\
			</div>\
		</li>\
	{{/inout}}\
	</ul>';


var tempPercorso='\
<ul class="app-list">\
<label>Percorso</label>\
{{#data}}\
	<li>{{b_datetime}} - {{b_type}} - {{b_speed}} - {{b_acceleration}}</li>\
{{/data}}\
</ul>';