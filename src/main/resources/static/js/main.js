$(document).ready(function() {

	$('.ng-datatable-apikeys').DataTable({
		"columnDefs": [{"orderable": false, "targets": [ 4 ]}]
	});	
	
	
	var editor; // use a global for the submit and return data rendering in the examples
	 
	$(document).ready(function() {
	    editor = new $.fn.dataTable.Editor( {
	        ajax: {
	            create: {
	                type: 'POST',
	                url:  '/api/TODO'
	            },
	            edit: {
	                type: 'PUT',
	                url:  '/api/TODO'
	            },
	            remove: {
	                type: 'DELETE',
	                url:  '/api/TODO'
	            }
	        },
	        table: "#leagueinformer-datatable-accounts",
	        fields: [ {
	                label: "id:",
	                name: "id"
	            }, {
	                label: "username",
	                name: "username"
	            }, {
	                label: "password:",
	                name: "password"
	            }, {
	                label: "region:",
	                name: "region"
	            }, {
	                label: "enabled:",
	                name: "enabled"
	            }
	        ]
	    } );
	 
	    $('#leagueinformer-datatable-accounts').DataTable( {
	        dom: "Bfrtip",
	        ajax: "/api/accounts/",
	        columns: [
	            { data: "id" },
	            { data: "username<" },
	            { data: "password" },
	            { data: "region" },
	            { data: "enabled"}
	        ],
	        select: true,
	        buttons: [
	            { extend: "create", editor: editor },
	            { extend: "edit",   editor: editor },
	            { extend: "remove", editor: editor }
	        ]
	    } );
	} );
	
} );


