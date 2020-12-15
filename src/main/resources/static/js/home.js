/*
 * JavaScript file for the application to demonstrate
 * using the API
 */

// Create the namespace instance
let ns = {};

// Create the model instance
ns.model = (function () {
    'use strict';

    let start_date = new Date(new Date() - 30 * 24 * 60 * 60 * 1000)
        .toISOString()
        .substr(0, 10);
    let $event_pump = $('body');

    // Return the API
    return {
        'read': function () {
            let ajax_options = {
                type: 'GET',
                url: 'sec',
                data: {startfilingdate: start_date, length: 5000},
                config: {headers: {"Content-Type": "multipart/form-data"}},
                accept: 'application/json',
                dataType: 'json'
            };
            $.ajax(ajax_options)
                .done(function (data) {
                    $event_pump.trigger('model_read_success', [data]);
                })
                .fail(function (xhr, textStatus, errorThrown) {
                    $event_pump.trigger('model_error', [xhr, textStatus, errorThrown]);
                })
        },
        update: function () {
            let ajax_options = {
                type: 'PUT',
                url: 'sec'
            };
            $.ajax(ajax_options)
                .done(function (data) {
                    $event_pump.trigger('model_update_success', [data]);
                })
                .fail(function (xhr, textStatus, errorThrown) {
                    $event_pump.trigger('model_error', [xhr, textStatus, errorThrown]);
                })
        }
    };
}());

// Create the view instance
ns.view = (function () {
    'use strict';

    let
        $OrganizationName = $('#OrganizationName'),
        $FormName = $('#FormName'),
        $CIK = $('#CIK'),
        $FilingDate = $('#FilingDate'),
        $SECUrl = $('#SECUrl');

    // return the API
    return {
        reset: function () {
            $OrganizationName.val('').focus();
            $FormName.val('');
            $FilingDate.val('');
            $SECUrl.val('');
            $CIK.val('');
        },
        update_editor: function (OrganizationName, FormName, CIK, FilingDate, SECUrl) {
            $OrganizationName.val(OrganizationName).focus();
            $FormName.val(FormName);
            $CIK.val(CIK);
            $FilingDate.val(FilingDate);
            $SECUrl.val(SECUrl);
        },
        build_table: function (seclist) {

            let rows = ''

            // clear the table
            $('.seclist table > tbody').empty();

            // did we get a seclist array?
            if (seclist) {
                for (let i = 0, l = seclist.length; i < l; i++) {
                    rows += `<tr> 
                                    <td class="leftIndentedCell">${seclist[i].organizationName}</td>
                                    <td class="leftIndentedCell">${seclist[i].formName}</td>
                                    <td class="leftIndentedCell">${seclist[i].cik}</td>
                                    <td class="leftIndentedCell">${seclist[i].filingDate}</td>
                                    <td class="leftIndentedCell"><a target="_blank" rel="noopener noreferrer" href="${seclist[i].secUrl}">${seclist[i].secUrl}</a></td>
                            </tr>`;
                }
                $('table > tbody').append(rows);
            }

            // Pagination and Search Capabilities
            $(document).ready(function () {
                $('#data_table').DataTable();
            });
        },
        error: function (error_msg) {
            $('.error')
                .text(error_msg)
                .css('visibility', 'visible');
            setTimeout(function () {
                $('.error').css('visibility', 'hidden');
            }, 3000)
        }
    };
}());

// Create the controller
ns.controller = (function (m, v) {
    'use strict';

    let model = m,
        view = v,
        $event_pump = $('body');

    // Get the data from the model after the controller is done initializing
    setTimeout(function () {
        model.read();
    }, 100)


    $('#update').click(function (e) {
        model.update()
        e.preventDefault();
    });

    // Create our event handlers
    $('table > tbody').on('dblclick', 'tr', function (e) {
        let $target = $(e.target),
            OrganizationName,
            FormName,
            CIK,
            FilingDate,
            SECUrl;

        OrganizationName = $target
            .parent()
            .find('td.OrganizationName')
            .text();

        FormName = $target
            .parent()
            .find('td.FormName')
            .text();

        CIK = $target
            .parent()
            .find('td.CIK')
            .text();

        FilingDate = $target
            .parent()
            .find('td.FilingDate')
            .text();

        SECUrl = $target
            .parent()
            .find('td.SECUrl')
            .text();

        view.update_editor(OrganizationName, FormName, CIK, FilingDate, SECUrl);
    });

    // Handle the model events
    $event_pump.on('model_read_success', function (e, data) {
        view.build_table(data);
        view.reset();
    });

    $event_pump.on('model_update_success', function (e, data) {
        view.build_table(data);
        view.reset();
    });

    $event_pump.on('model_error', function (e, xhr, textStatus, errorThrown) {
        let error_msg = textStatus + ': ' + errorThrown + ' - ' + xhr.status + ' - ' + xhr.responseText;
        view.error(error_msg);
        console.log(error_msg);
    })
}(ns.model, ns.view));


