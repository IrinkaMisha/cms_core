(function ($) {
    var
        _moduleName = "adminMenu",
        _version = "0.0.2-Snapshot",
        _description = "Модуль для отображения меню администрирования",
        _storage = {dc: undefined, namesContainers: undefined, dcAllContainers: null, profileRoles: null},
        _debug = true,
        context = ctx || "" + "/",// default test from inHeader.jsp
        _urls = {
            exampleUrl: context + "exampleUrl", // урл для ...
            dc: context + "ajax/api1/dc/get/", // урл для получения entity DinamicContent
            namesContainers: context + "ajax/api1/dc/namesContainers", // урл для получения имен переменных которые можно использовать для вставки контейнеров
            dcsave: context + "ajax/api1/dc/save", // урл для сохранения контента
            createnew: context + "ajax/api1/dc/create", // урл для создания нового контента через аякс
            dcAllContainers: context + "ajax/api1/dc/all/container", // урл для получения всех контейнеров
            allTemplates: context + "ajax/api1/dc/templates/all", // урл для получения всех шаблонов
            showContent: context + "content/show/", // урл для отображения контейнеров
            showTemplate: context + "template/show/", // урл для отображения шаблона
            profileUsers: context + "ajax/api1/profile/users-with-roles", // урл для users
            profileUserDelete: context + "ajax/api1/profile/user/delete/", // урл для delete
            profileUserSave: context + "ajax/api1/profile/user/save/", // урл для save
            //profileUserChangeActive: context + "ajax/api1/profile/user/changeActive", // урл для user changeActive
            profileRoles: context + "ajax/api1/profile/roles", // урл для roles
            profileRoleSave: context + "ajax/api1/profile/role/save", // урл для role save
            profileRoleDelete: context + "ajax/api1/profile/role/delete", // урл для role delete
            profilePermissions: context + "ajax/api1/profile/permissions", // урл для permissions

            jqui: "/webjars/jquery-ui/1.11.3/jquery-ui.min.js",
            bcss: "/webjars/bootstrap/3.3.4/css/bootstrap.min.css",
            bjs: "/webjars/bootstrap/3.3.4/js/bootstrap.js"
        },
        selectors = {
            dialogAdd: "#dialog-form-add", // диалоговое окно редактирования
            dialogRoleFirst: "#dialog-role-first", // диалоговое окно роли
            dialogUsersFirst: "#dialog-users-first", // диалоговое окно роли
            positionAddContent: "#dialog-form-add-content", // место для добавления в диалоговое окне редактирования
            allcont: "#selcontainers", // селектор для выбора из списка всех доступных контейнеров
            framecont: "#framecont", // селектор iframe отображения
            frame2: "#frame2", // селектор iframe2 отображения
            razmetka: "#razmetka", // выбор шаблона
            editor: "editor1", // выбор for ckeditor
            nameDC: "nameDC", // имя создоваемого контейнера
            wrapeditor: "#wrappereditor1", // wrapper for ckeditor
            shName: "#shName",// селектор  - select выбора шаблонов
            roleList: "#roleList",// селектор  - select для списка ролей
            listUsersForDelete: "#listUsersForDelete",// селектор  - select для списка ролей
            tPermissions: "#tPermissions",// селектор  - списка permissions
            tmpFirst: "#tmpFirst",// селектор теневого шаблона - редактирования блока
            tmpDialogOne: "#tmpDialogOne",// селектор теневого шаблона - диалоговое окно
            tmpDialogRoleFirst: "#tmpDialogRoleFirst",// селектор теневого шаблона - диалоговое окно
            tmpDialogUsersFirst: "#tmpDialogUsersFirst",// селектор теневого шаблона - диалоговое окно
            tmpMenuAdmin: "#tmpMenuAdmin",// селектор теневого шаблона - меню админка
            adminmenu: "#adminmenu",// селектор adminmenu
            btnRoleSave: "#btn-role-save"// селектор кнопки сохранения
        },
        typeDynCont = {page: "page", container: "container", text: "text", tiles: "tiles"}
        ,
        menuData = {
            items: {
                "edit": {name: "Редактировать", icon: "edit"},
                //"add": {name: "чета блок", icon: "add"},
                //"sep1": "---------",
                //"dead": {name: "Удалить?", icon: "quit"}
            }
        },
        currData = {
            dyncon: undefined,
            names: undefined,
            blockName: undefined,
            editor: undefined,
            oldValue: {},
            ndc: undefined
        },
        typeAlerts = {
            alert: "alert",
            information: "information",
            error: "error",
            warning: "warning",
            notification: "notification",
            success: "success"
        },
        dialogAdd, dialogRoleFirst, dialogUsersFirst, dialogDcFirst, dialogsInfo = {
            isOpenRole: false,
            isOpenUsersFirst: false,
            isOpenDcFirst: false,
            isCreatedRoleFirst: false,
            isCreatedUsersFirst: false,
            isCreatedDcFirst: false
        }, jqbody
        ;

    window.log = function () {
        if (_debug) {
            try {
                return console.log.apply(console, arguments);
            } catch (_error) {
            }
        }
    };

    if (typeof jQuery === 'undefined') {
        throw new Error(_moduleName + ' JavaScript requires jQuery')
    } else {
        log("add to Jquery new function filterByText");
        jQuery.fn.filterByText = function (textbox, selectSingleMatch) {
            return this.each(function () {
                var select = this;
                var options = [];
                $(select).find('option').each(function () {
                    options.push({value: $(this).val(), text: $(this).text()});
                });
                $(select).data('options', options);
                $(textbox).bind('change keyup', function () {
                    var options = $(select).empty().scrollTop(0).data('options');
                    var search = $.trim($(this).val());
                    var regex = new RegExp(search, 'gi');

                    $.each(options, function (i) {
                        var option = options[i];
                        if (option.text.match(regex) !== null) {
                            $(select).append(
                                $('<option>').text(option.text).val(option.value)
                            );
                        }
                    });
                    if (selectSingleMatch === true &&
                        $(select).children().length === 1) {
                        $(select).children().get(0).selected = true;
                    }
                });
            });
        };
    }

    function Api() {
        this.info();
        jqbody = $("body");
        if (_debug) {
            log("дебаг режим для " + _moduleName);
            podsvetka();
            //showDialogUsersFirst();
        }
        init();
    }

    Api.prototype = {
        constructor: Api,
        moduleName: _moduleName,
        description: _description,
        version: _version,
        storage: _storage,
        info: function () {
            console.log(this.moduleName + " ver:" + this.version + " : " + _description);
        },
        init: init,
        addMenuSettings: addMenuSettings,
        podsvetka: podsvetka
    };

    function init() {

        //Для корректного отображения страниц в iframe в режиме sandbox пришлось применять теневой DOM , поэтому приходиться необходимые объекты добавлять в документ вручную

        var template = getTemplateAsString(selectors.tmpDialogOne); // получаем шаблок как строку, иначе замена не сработает
        var contentTemplate = useTemplate(template, {}); // добавляем переменные в шаблон - ну или если их нет
        jqbody.append(contentTemplate); // добавляем к документу

        template = getTemplateAsString(selectors.tmpMenuAdmin);
        contentTemplate = useTemplate(template, {});
        jqbody.append(contentTemplate);

        dialogAdd = $(selectors.dialogAdd).dialog({
            autoOpen: false,
            height: 600,
            width: 800,
            closeOnEscape: true,
            modal: true,
            buttons: {
                "Сохранить и перегрузить страницу": prepareSendData
                //,"test": testFunc
            },
            close: function () {

            }
        }).submit(function (e) {
            e.preventDefault();
            prepareSendData();
        });
        ajaxGetAllConteiners();
        showSelectShablon();
        createMenuAdminAndItemHandlers();
        addEditIconToBlocks();
    }

    function createMenuAdminAndItemHandlers() {
        var adminmenu = $(selectors.adminmenu);
        adminmenu.menu();
        adminmenu.on("menuselect", function (event, ui) {
            var idmenu = ui.item.attr("id");
            switch (idmenu) {
                case "test":

                    break;
                case "role.first":
                    showDialogRoleFirst();
                    break;
                case "users.first":
                    showDialogUsersFirst();
                    break;
                case "dc.show":
                    showDialogDynContFirst();
                    break;
                case "backlight":
                    podsvetka();
                    break;
                default:
                //console.log(idmenu);
            }
        });
        $('ul li a.menudisabled').addClass('ui-state-disabled').off('click');
    }

    function createDialogRoleFirst() {
        if (!dialogsInfo.isCreatedRoleFirst) {
            jqbody.append(useTemplate(getTemplateAsString(selectors.tmpDialogRoleFirst), {})); // добавление окна для роли
            dialogRoleFirst = $(selectors.dialogRoleFirst).dialog({
                autoOpen: false,
                height: 600,
                width: 800,
                closeOnEscape: true,
                modal: true,
                buttons: {
                    "закрыть": function () {
                        dialogRoleFirst.dialog("close");
                    }
                },
                close: function () {
                    dialogsInfo.isOpenRole = false;
                }
            }).submit(function (e) {
                e.preventDefault();
                //dialogRoleFirst.dialog("close");
            });

            //,"test": testFunc
            $(selectors.btnRoleSave).click(handlerRoleChange);
            $("#btn-role-create").click(handlerRoleCreate);
            $("#btn-role-delete").click(handlerRoleDelete);
            dialogsInfo.isCreatedRoleFirst = true;
        }
    }

    function handlerRoleChange() {
        var newPermissions = [];
        // пробегаем по списку разрешений и сохраняем те, которые были отмечены чекбосами
        var tPermissions = $(selectors.tPermissions + ">tr");
        var id = $(selectors.roleList).val();
        tPermissions.each(function (index) {
            var checkbox = $("td>input[type='checkbox']", this);
            var chek = checkbox.prop('checked');
            if (chek) {
                var permission = $(this).text();
                newPermissions.push(permission);
            }
            //console.log( index + ": " + $( this ).text() );
        });
        var changedObjVO = {id: id, name: null, permissions: newPermissions}
        ajaxRoleUpdate(changedObjVO).done(function (data) {
            showDialogRoleFirst();
            generate("Изменение роли:" + data.name + " выполнено", typeAlerts.success, 5000);
        });

    }

    function handlerRoleCreate() {
        var newPermissions = [];
        // пробегаем по списку разрешений и сохраняем те, которые были отмечены чекбосами
        var tPermissions = $(selectors.tPermissions + "2" + ">tr");
        var newRoleName = $("#name-new-role").val();
        if (newRoleName === "") {
            generate("Вы не задали имя роли!", typeAlerts.information, 5000);
            return;
        }
        if (isNameRoleExist(newRoleName)) {
            generate("Роль с таким именем уже существует", typeAlerts.information, 5000);
            return;
        }
        $("#btn-role-create").attr("disabled", true);
        tPermissions.each(function (index) {
            var checkbox = $("td>input[type='checkbox']", this);
            var chek = checkbox.prop('checked');
            if (chek) {
                var permission = $(this).text();
                newPermissions.push(permission);
            }
        });
        var changedObjVO = {id: null, name: newRoleName, permissions: newPermissions};
        ajaxRoleUpdate(changedObjVO).done(function (data) {
            showDialogRoleFirst();
            generate("создание роли:" + data.name + " выполнено", typeAlerts.success, 5000);
            $("#btn-role-create").attr("disabled", false);
            //log(data);
        });
    }

    function handlerRoleDelete() {
        var selDel = $("#roleList2");
        var id = selDel.val();
        if (id === "sel") {
            generate("Вы не выбрали роль для удаления!", typeAlerts.information, 5000);
            return;
        }

        var name = $('[value="' + id + '"]', selDel).text();
        var changedObjVO = {id: id, name: name};
        currData.removableRole = changedObjVO;
        generateWithButton("Вы хотите удалить роль:" + name, typeAlerts.warning, doRoleDelete);
    }

    function doRoleDelete() {
        $("#btn-role-delete").attr("disabled", true);
        //log("был удален:" + JSON.stringify(data));
        ajaxRoleDelete(currData.removableRole).done(function (data) {
            showDialogRoleFirst();
            $("#btn-role-delete").attr("disabled", false);
            generate("Вы удалили роль:" + data.name, typeAlerts.success, 5000);
            //log("был удален:"+JSON.stringify(data));
        });

    }


    function showDialogRoleFirst() {
        createDialogRoleFirst();
        $("#name-new-role").empty();
        var selRoles = $(selectors.roleList);
        var selRoles2 = $(selectors.roleList + "2");
        var tPermissions = $(selectors.tPermissions);
        var tPermissions2 = $(selectors.tPermissions + "2");
        selRoles.empty();
        selRoles2.empty();
        selRoles2.append('<option value="' + "sel" + '">' + "выберите роль" + '</option>');
        tPermissions.empty();
        tPermissions2.empty();
        $.when(ajaxGetAllRoles(), ajaxGetAllPermissions()).done(function (roles, permissions) {
            roles = roles[0];
            permissions = permissions[0];

            for (var d = 0; d < roles.length; d++) {
                selRoles.append('<option value="' + roles[d].id + '">' + roles[d].name + '</option>');
                selRoles2.append('<option value="' + roles[d].id + '">' + roles[d].name + '</option>');
            }

            for (var d = 0; d < permissions.length; d++) {
                tPermissions.append('<tr><td><input type="checkbox"></td><td>' + permissions[d] + '</td></tr>');
                tPermissions2.append('<tr><td><input type="checkbox"></td><td>' + permissions[d] + '</td></tr>');
            }
            selRoles.change(roleChange);
            selRoles.change();
            //selRoles2.change();
            //$('#roleTabs a[href="#redit"]').tab('show'); // заменил css
            if (!dialogsInfo.isOpenRole) {
                dialogsInfo.isOpenRole = true;
                dialogRoleFirst.dialog("open");
            }
        }).fail(handleAjaxError);
    }

    function isNameRoleExist(name) {
        var role;
        for (var i = 0; i < _storage.profileRoles.length; i++) {
            role = _storage.profileRoles[i];
            if (role.name === name) {
                return true;
            }
        }
        ;
        return false;
    }

    function roleChange() {
        var id = $(this).val();
        var tPermissions = $(selectors.tPermissions + ">tr");
        var role = getObjectFromList(id, _storage.profileRoles);
        var listRolePermissions = getListPermissions(role);
        //log(JSON.stringify(role));
        tPermissions.each(function (index) {
            var checkbox = $("td>input[type='checkbox']", this);
            //var chek = checkbox.prop('checked');
            var permission = $(this).text();
            if (containtsInList(permission, listRolePermissions)) {
                checkbox.prop('checked', true);
            } else {
                checkbox.prop('checked', false);
            }
            //console.log( index + ": " + $( this ).text() );
        });
    }

    function createDialogUsersFirst() {
        if (!dialogsInfo.isCreatedUsersFirst) {
            jqbody.append(useTemplate(getTemplateAsString(selectors.tmpDialogUsersFirst), {})); // добавление окна для роли
            dialogUsersFirst = $(selectors.dialogUsersFirst).dialog({
                autoOpen: false,
                height: 650,
                width: 800,
                closeOnEscape: true,
                modal: true,
                buttons: {
                    "закрыть": function () {
                        dialogUsersFirst.dialog("close");
                    }
                },
                close: function () {
                    dialogsInfo.isOpenUsersFirst = false;
                }
            }).submit(function (e) {
                e.preventDefault();
            });
            dialogsInfo.isCreatedUsersFirst = true;

            $("#btn-user-change").click(handlerBtnUserChange);
            $("#btn-restore").click(handlerBtnUserRestore);
            //$("#btn-role-create").click(handlerRoleCreate);
            $("#btn-user-delete").click(handlerUserDelete);
        }
    }

    function handlerBtnUserRestore() {
        var id = $("#listUsersForChange>option:selected").attr("usid");
        //var user = getObjectFromList(id, _storage.profileUsers );
        var changedObjVO = {id: id, restore: true};
        ajaxUserUpdate(changedObjVO).done(function (data) {
            showDialogUsersFirst();
            generate("Восстановление пользователя:" + data.name + " выполнено", typeAlerts.success, 5000);
        });

    }

    function handlerBtnUserChange() {
        var isActive = $("#user-isActive").prop('checked');
        log(isActive);
        var id = $("#listUsersForChange>option:selected").attr("usid");
        var user = getObjectFromList(id, _storage.profileUsers);
        var newRoles = [];
        // пробегаем по списку Ролей и сохраняем те, которые были отмечены чекбосами
        var tRoles = $("#tUserRoles>tr");
        tRoles.each(function () {
            var checkbox = $("td>input[type='checkbox']", this);
            var chek = checkbox.prop('checked');
            if (chek) {
                var idRole = checkbox.val();
                //var permission = $(this).text();
                newRoles.push(idRole);
            }
            //console.log( index + ": " + $( this ).text() );
        });
        var changedObjVO = {id: id, active: isActive, roles: newRoles};
        ajaxUserUpdate(changedObjVO).done(function (data) {
            showDialogUsersFirst();
            generate("Изменение пользователя:" + data.name + " выполнено", typeAlerts.success, 5000);
        });

    }

    function showDialogUsersFirst() {
        createDialogUsersFirst();
        var listUsersForDelete = $(selectors.listUsersForDelete);
        var listUsersForChange = $("#listUsersForChange");
        var tUserRoles = $("#tUserRoles");
        listUsersForDelete.empty();
        listUsersForDelete.append('<option value="' + "sel" + '">' + "выберите пользователя" + '</option>');
        listUsersForChange.empty();
        tUserRoles.empty();
        $.when(ajaxGetAllUsers(), ajaxGetAllRoles()).done(function (users, roles) {
            users = users[0];
            roles = roles[0];
            //drawTableUsersForEdit("#tableUsersEdit", users);
            if (dialogsInfo.isOpenUsersFirst) {
                $("#tableUsersEdit").jqGrid('setGridParam', {
                    datatype: 'local',
                    data: users,
                    sortname: 'name',
                    sortorder: 'asc'
                }).trigger('reloadGrid');
            }
            var cssName = "";
            for (var d = 0; d < users.length; d++) {
                cssName = "noactive";
                if (!users[d].removed) {
                    listUsersForDelete.append('<option value="' + users[d].id + '">' + users[d].name + '</option>');
                }
                if (users[d].active) {
                    cssName = "active";
                }
                if (users[d].removed) {
                    cssName = "removed";
                }
                listUsersForChange.append('<option class="' + cssName + '" usid="' + users[d].id + '" value="' + users[d].name + '">' + users[d].name + '</option>');
                //selRoles2.append('<option value="' + roles[d].id + '">' + roles[d].name + '</option>');
            }
            for (var j = 0; j < roles.length; j++) {
                tUserRoles.append('<tr><td><input value="' + roles[j].id + '" type="checkbox"></td><td>' + roles[j].name + '</td></tr>');
            }
            $('#listUsersForChange').filterByText($("#filterUsers"), false);
            //
            //for (var d = 0; d < permissions.length; d++) {
            //    tPermissions.append('<tr><td><input type="checkbox"></td><td>' + permissions[d] + '</td></tr>');
            //    tPermissions2.append('<tr><td><input type="checkbox"></td><td>' + permissions[d] + '</td></tr>');
            //}
            listUsersForChange.change(handlerListUsersChange);
            listUsersForChange.change();
            //selRoles2.change();
            //$('#roleTabs a[href="#redit"]').tab('show'); // заменил css
            if (!dialogsInfo.isOpenUsersFirst) {
                dialogsInfo.isOpenUsersFirst = true;
                dialogUsersFirst.dialog("open");
            }
        }).fail(handleAjaxError);
    }

    function handlerListUsersChange() {
        var id = $('option:selected', this).attr('usid');
        var user = getObjectFromList(id, _storage.profileUsers);
        var tUserRoles = $("#tUserRoles" + ">tr");
        if (user.removed) {
            $("#user-sett").addClass("hidden");
            $("#btn-restore").removeClass("hidden");
            $("#btn-user-change").prop("disabled", true);
            $("#btn-restore").prop("disabled", false);
        } else {
            $("#user-sett").removeClass("hidden");
            $("#btn-restore").addClass("hidden");
            $("#btn-user-change").prop("disabled", false);
            $("#btn-restore").prop("disabled", true);
        }
        //var listRolePermissions = getListPermissions(role);
        //log(JSON.stringify(role));
        tUserRoles.each(function (index) {
            var checkbox = $("td>input[type='checkbox']", this);
            //var chek = checkbox.prop('checked');
            var idRole = checkbox.val();
            var roleObj = getObjectFromList(idRole, user.roles);
            if (roleObj) {
                checkbox.prop('checked', true);
            } else {
                checkbox.prop('checked', false);
            }
            //console.log( index + ": " + $( this ).text() );
        });

        var userIsActive = $("#user-isActive");
        if (user.active) userIsActive.prop('checked', true);
        else userIsActive.prop('checked', false);
    }

    //function drawTableUsersForEdit(tagId, datas) {
    //    //$.jgrid.defaults.styleUI = 'Bootstrap';
    //    $(tagId).jqGrid({
    //        data: datas,
    //        rowNum: 15,
    //        datatype: "local",
    //        rownumbers: false,
    //        hidegrid: false,
    //        height: "100%",
    //        sortname: 'name', sortorder: 'asc',
    //        //weidt:600,
    //        //rowNum: 9990,
    //        rowList: [10, 20, 50, 100],
    //        colNames: ['id', 'active', 'name'],
    //        colModel: [
    //            {name: 'id', width: 100, align: "center", sorttype: "int"},
    //            {
    //                name: 'active',
    //                index: 'active',
    //                width: 100,
    //                align: "center",
    //                formatter: myCustomHandlerForFormatter
    //                //formatoptions: {disabled: false},
    //                //edittype: "checkbox",
    //                //editable:true
    //
    //            },
    //            {name: 'name', index: 'name', width: 400, align: "left", sorttype: "string"}
    //        ],
    //        pager: "#gridpager",
    //        search: true,
    //        viewrecords: true,
    //        caption: "Пользователи"
    //    });
    //
    //    //$(tagId).bind("jqGridAfterSaveCell", function (e, rowid, orgClickEvent) {
    //    //    log(arguments);
    //    //    // if we want to return true, we should test e.result additionally
    //    //    return e.result === undefined ? true : e.result;
    //    //});
    //
    //
    //    function myCustomHandlerForFormatter(cellvalue, options, rowObject) {
    //        //log("myCustomHandlerForFormatter",cellvalue, options, rowObject);
    //
    //        var cheked = rowObject.active ? "checked" : "";
    //        // do something here
    //        return "<input type='checkbox' value='" + rowObject.id + "' onchange='adminMenu.cboxHandler(" + rowObject.id + ");' " + cheked + ">";
    //    };
    //
    //
    //    //jQuery(".jqgrow td input").each(function(){
    //    //    jQuery(this).click(function(){
    //    //        log(arguments);
    //    //    });
    //    //});
    //
    //}

    //function cboxHandler(id) {
    //    //var obj = getObjectFromList(id, _storage.profileUsers);
    //    //obj.active = !obj.active;
    //    ajaxUserChangeActive(id).done(function (data) {
    //        generate("Изменение пользователя:" + data.name + " выполнено", typeAlerts.success, 5000);
    //        showDialogUsersFirst();
    //    });
    //}

    function handlerUserDelete() {
        var selDel = $(selectors.listUsersForDelete);
        var id = selDel.val();
        if (id === "sel") {
            generate("Вы не выбрали пользователя для удаления!", typeAlerts.information, 5000);
            return;
        }
        var name = $('[value="' + id + '"]', selDel).text();
        currData.removableUser = {id: id, name: name};
        generateWithButton("Вы хотите удалить пользователя:" + name, typeAlerts.warning, doUserDelete);
    }

    function doUserDelete() {
        $("#btn-user-delete").attr("disabled", true);
        ajaxUserDelete(currData.removableUser.id).done(function (data) {
            showDialogUsersFirst();
            $("#btn-user-delete").attr("disabled", false);
            generate("Вы удалили пользователя:" + data.name, typeAlerts.success, 5000);
        });
    }

    function createDialogDynConFirst() {
        if (!dialogsInfo.isCreatedDcFirst) {
            jqbody.append(useTemplate(getTemplateAsString("#tmpDialogDynamicContent"), {})); // добавление окна для dc
            dialogDcFirst = $("#dialog-dc-first").dialog({
                autoOpen: false,
                height: 650,
                width: 1000,
                closeOnEscape: true,
                modal: true,
                buttons: {
                    "закрыть": function () {
                        dialogDcFirst.dialog("close");
                    }
                },
                close: function () {
                    dialogsInfo.isOpenDcFirst = false;
                }
            }).submit(function (e) {
                e.preventDefault();
            });
            dialogsInfo.isCreatedDcFirst = true;
            $("#selTypeDc").change(function () {
                var type = $(this).val();
                $("#tableDc").jqGrid("setGridParam", {url: '/ajax/api1/dc/all/' + type}).trigger("reloadGrid");
            });
            //$("#btn-user-change").click(handlerBtnUserChange);
            //$("#btn-restore").click(handlerBtnUserRestore);
            ////$("#btn-role-create").click(handlerRoleCreate);
            //$("#btn-user-delete").click(handlerUserDelete);
        }
    }

    function showDialogDynContFirst() {
        createDialogDynConFirst();
        drawTableDcFirst("#tableDc", "#tableDcPager", "page");
        if (!dialogsInfo.isOpenDcFirst) {
            dialogsInfo.isOpenDcFirst = true;
            dialogDcFirst.dialog("open");
        }

    }

    function drawTableDcFirst(tagId, tagIdPagger, typeDc) {
        var template = "<div style='margin-left:15px;width:400px'>";
        template += "<div>  Name: </div><div>{name} </div>";
        template += "<div> content: </div><div>{content} </div>";
        template += "<hr style='width:100%;'/>";
        template += "<div> {sData} {cData}  </div></div>";


        //template = getTemplateAsString("#");
        $(tagId).jqGrid({
            url: '/ajax/api1/dc/all/' + typeDc,
            datatype: "json",
            colModel: [
                {
                    label: 'Id DC', name: 'id', width: 60, formatter: myFormatterDC
                    //formatoptions: {
                    //    url: function (cellValue, rowId, rowData) {
                    //        return '/content/show/' + rowData.id + '?';
                    //        //+
                    //            //$.param({
                    //            //    quantity: rowData.Stocks_valkogus
                    //            //});
                    //    }
                    //}
                },
                {label: 'Name DC', name: 'name', width: 300},
                {label: 'Description DC', name: 'content', width: 350},
                {label: 'Type DC', name: 'typeDynCont', width: 80},
                {label: 'shablon', name: 'shablonName', width: 65}
            ],
            viewrecords: true, // show the current page, data rang and total records on the toolbar
            //width: 780,
            width: "100%",
            height: "100%",
            rowList: [20, 50, 100],
            rowNum: 20,
            //loadonce: true, // this is just for the demo
            pager: tagIdPagger
        });
        function myFormatterDC(cellvalue, options, rowObject) {
            log("myFormatterDC",cellvalue, options, rowObject);

            //var cheked = rowObject.active ? "checked" : "";
            // do something here
            return "<a href='/content/show/"+ rowObject.id + "' target='_blank'>"+rowObject.id+"</a>";
        };

        $(tagId).navGrid(tagIdPagger,
            // the buttons to appear on the toolbar of the grid
            {
                edit: false,
                add: false,
                del: true,
                search: false,
                refresh: false,
                view: false,
                position: "left",
                cloneToTop: true
            },
            // options for the Edit Dialog
            {
                editCaption: "The Edit Dialog",
                template: template,
                errorTextFormat: function (data) {
                    return 'Error: ' + data.responseText
                }
            },
            // options for the Add Dialog
            {
                template: template,
                errorTextFormat: function (data) {
                    return 'Error: ' + data.responseText
                }
            },
            // options for the Delete Dailog
            {
                errorTextFormat: function (data) {
                    return 'Error: ' + data.responseText
                }
            });

    }


    function containtsInList(permission, list) {
        for (var i = 0; i < list.length; i++) {
            if (list[i] === permission) {
                return true;
            }
        }
        return false;
    }

    function getObjectFromList(id, list) {
        if (typeof id === 'string') {
            id = parseInt(id);
        }
        for (var i = 0; i < list.length; i++) {
            if (list[i].id === id) {
                return list[i];
            }
        }
    }

    function getListPermissions(role) {
        var list = [];
        for (var i = 0; i < role.nodeProperties.length; i++) {
            if (role.nodeProperties[i].keyt === "credential") {
                list.push(role.nodeProperties[i].value);
            }
        }
        return list;
    }

    function showSelectShablon() {
        var selShab = $(selectors.shName);
        //selShab.append('<option value="null">&lt;Шаблон не выбран&gt;</option>');
        $.ajax({
            url: _urls.allTemplates,
            dataType: "json",
            type: "GET"
        }).done(function (data) {
            selShab.empty();
            if (data.length < 1) {
                selShab.append('<option value="empty">&lt;Шаблоны отсутствуют&gt;</option>');
            } else {
                for (var d = 0; d < data.length; d++) {
                    if (data[d].type === "page") continue;  // показываем шаблоны только для контейнеров
                    selShab.append('<option value="' + data[d].name + '">' + data[d].name + '</option>');
                }
            }
            selShab.change(function () {
                var iframe2 = $(selectors.frame2);
                var val = $(this).val();
                if (val != "empty" && val) {
                    iframe2.attr("src", _urls.showTemplate + val);

                    $('div.block-empty', iframe2).prepend("<span class='container-setting ui-icon ui-icon-wrench'></span>").addClass("select-empty");
                }
            });
        });
    }

    function showSelectorContainers(selector) {
        var selectC = $(selector);
        var alldc = _storage.dcAllContainers;
        selectC.empty().append('<option value="empty">пустой контейнер или очистить</option>').
            append('<option value="new">Создать новый на основе шаблона</option>');
        for (var d = 0; d < alldc.length; d++) {
            if (_debug) {
                selectC.append('<option value="' + alldc[d].id + '">' + alldc[d].name + "(" + alldc[d].id + ")" + '</option>');
            } else {
                selectC.append('<option value="' + alldc[d].id + '">' + alldc[d].name + '</option>');
            }
        }

        selectC.change(function () {
            var val = $(this).val();
            var iframe = $(selectors.framecont);
            if (val != "empty" && val != "new" && val) {
                iframe.attr("src", _urls.showContent + val);
                iframe.removeClass("hidden");
                $(selectors.razmetka).addClass("hidden");
            }
            if (val == "empty") {
                iframe.attr("src", "");
                $(selectors.razmetka).addClass("hidden");
            }
            if (val == "new") {
                iframe.addClass("hidden");
                $(selectors.razmetka).removeClass("hidden");

            }
        });
    }

    function handleAjaxError(jqXHR, textStatus, errorThrown) {
        log("Ошибка выполнения запроса:", jqXHR, textStatus, errorThrown);
        generate("Ошибка выполнения запроса" + "<br>requestStatus:" + jqXHR.statusText + "<br>textStatus:" + textStatus + "<br>message:" + errorThrown.message, typeAlerts.error);
    }

    function ajaxNamesContainers(dcId, callback) {
        log('вызов ajaxNamesContainers');
        return $.ajax({
            url: _urls.namesContainers,
            dataType: 'json',
            type: "GET",
            data: {idCont: dcId}
        }).done(function (data) {
                _storage.namesContainers = data;
                //log(_moduleName + "-ajaxNamesContainers:" + JSON.stringify(data));
                if (callback) callback(data);
            }
        );
    }

    function ajaxDinamicContent(dcId, callback) {
        log('вызов ajaxDinamicContent');
        return $.ajax({
            url: _urls.dc + dcId,
            dataType: 'json',
            type: "GET"
        }).done(function (data) {
                _storage.dc = data;
                //log(_moduleName + "-ajaxDinamicContent:" + JSON.stringify(data));
                if (callback) callback(data);
            }
        );
    }

    //function ajaxUserChangeActive(idUser) {
    //    return $.ajax({
    //        url: _urls.profileUserChangeActive,
    //        dataType: "json",
    //        type: "POST",
    //        beforeSend: function (xhr) {
    //            xhr.setRequestHeader("Accept", "application/json");
    //            xhr.setRequestHeader("Content-Type", "application/json");
    //        },
    //        data: JSON.stringify(idUser)
    //    }).fail(handleAjaxError);
    //}
    function ajaxUserUpdate(userVO, callback) {
        return $.ajax({
            url: _urls.profileUserSave,
            dataType: "json",
            type: "POST",
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Accept", "application/json");
                xhr.setRequestHeader("Content-Type", "application/json");
            },
            data: JSON.stringify(userVO)
        }).fail(handleAjaxError);
    }

    function ajaxRoleUpdate(roleVO, callback) {
        return $.ajax({
            url: _urls.profileRoleSave,
            dataType: "json",
            type: "POST",
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Accept", "application/json");
                xhr.setRequestHeader("Content-Type", "application/json");
            },
            data: JSON.stringify(roleVO)
        }).fail(handleAjaxError);
        //.done(function (data) {
        //    if (callback) callback(data);
        //});
    }

    function ajaxRoleDelete(roleVO, callback) {
        return $.ajax({
            url: _urls.profileRoleDelete,
            dataType: "json",
            type: "POST",
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Accept", "application/json");
                xhr.setRequestHeader("Content-Type", "application/json");
            },
            data: JSON.stringify(roleVO)
        }).fail(handleAjaxError);
        //.fail(handleAjaxError)
        //.done(function (data) {
        //    if (callback) callback(data);
        //});
    }

    function ajaxUserDelete(id, callback) {
        return $.ajax({
            url: _urls.profileUserDelete + id,
            dataType: "json",
            type: "POST",
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Accept", "application/json");
                xhr.setRequestHeader("Content-Type", "application/json");
            }
        }).fail(handleAjaxError);
    }

    function ajaxSaveDynContent(dc) {
        return $.ajax({
            url: _urls.dcsave,
            dataType: "json",
            type: "POST",
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Accept", "application/json");
                xhr.setRequestHeader("Content-Type", "application/json");
            },
            data: JSON.stringify(dc)
        });
        //.done(function (data) {
        //        if (callback) callback(data);
        //    });
    }

    function ajaxCreateDynContent(dc, callback) {
        log('вызов ajaxCreateDynContent');
        return $.ajax({
            url: _urls.createnew,
            dataType: "json",
            type: "POST",
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Accept", "application/json");
                xhr.setRequestHeader("Content-Type", "application/json");
            },
            data: JSON.stringify(dc)
        }).done(function (data) {
            _storage.createnew = data;
            //log(_moduleName + "-ajaxCreateDynContent:" + JSON.stringify(data));
            if (callback) callback(data);
        });
    }

    function ajaxGetAllRoles(callback) {
        log('вызов ajaxGetAllRoles');
        return $.ajax({
            url: _urls.profileRoles,
            dataType: 'json',
            type: "GET"
        }).done(function (data) {
                _storage.profileRoles = data;
                //log(_moduleName + "-ajaxGetAllRoles:" + JSON.stringify(data));
                if (callback) callback(data);
            }
        );
    }

    function ajaxGetAllPermissions(callback) {
        log('вызов ajaxGetAllPermissions');
        return $.ajax({
            url: _urls.profilePermissions,
            dataType: 'json',
            type: "GET"
        }).done(function (data) {
                _storage.profileRoles = data;
                //log(_moduleName + "-ajaxGetAllPermissions:" + JSON.stringify(data));
                if (callback) callback(data);
            }
        );
    }

    function ajaxGetAllConteiners(callback) {
        log('вызов ajaxGetAllConteiners');
        return $.ajax({
            url: _urls.dcAllContainers,
            dataType: 'json',
            type: "GET"
        }).done(function (data) {
                _storage.dcAllContainers = data;
                //log(_moduleName + "-ajaxGetAllConteiners:" + JSON.stringify(data));
                if (callback) callback(data);
            }
        );
    }

    function ajaxGetAllUsers(callback) {
        log('вызов ajaxGetAllUsers');
        return $.ajax({
            url: _urls.profileUsers,
            dataType: "json",
            type: "GET"
        }).done(function (data) {
            _storage.profileUsers = data;
            //log(_moduleName + "-ajaxGetAllUsers:" + JSON.stringify(data));
            if (callback) callback(data);
        }).fail(handleAjaxError);
    }


    //function getRandomArbitrary(min, max) {
    //    return Math.random() * (max - min) + min;
    //}
    //
    //function getRandomInt(min, max) {
    //    return Math.floor(Math.random() * (max - min + 1)) + min;
    //}

    function useTemplate(template, data) {
        // template as String
        // data as Array or one object
        var i = 0,
            len = data.length,
            html = '';
        // Replace the {{XXX}} with the corresponding property
        function replaceWithData(data_bit) {
            var html_snippet, prop, regex;
            html_snippet = template;
            for (prop in data_bit) {
                regex = new RegExp('{{' + prop + '}}', 'ig');
                html_snippet = html_snippet.replace(regex, data_bit[prop]);
            }
            return html_snippet;
        }

        // Go through each element in the array and add the properties to the template
        for (; i < len; i++) { //as Array
            html += replaceWithData(data[i]);
        }
        if ((!len || len == 0 ) && data) { // as One Object
            html = replaceWithData(data);
        }
        // Give back the HTML to be added to the DOM as String
        return html;
    }

    function getTemplateAsString(selector) {
        var tmp2 = document.querySelector(selector).content;
        var child = "";
        for (var j = 0; j < tmp2.children.length; j++) {
            child += tmp2.children[j].outerHTML;
        }
        return child;
    }

    function getInfoAbautContainers(dynamicContent, blockName) {
        if (dynamicContent.cont.length < 1 || !blockName) {
            return {position: undefined, data: undefined};
        }
        var s;
        for (var i = 0; i < dynamicContent.cont.length; i++) {
            s = dynamicContent.cont[i];
            if (s && blockName === s.name) {
                return {position: i, data: s};
            }
        }
        return {position: undefined, data: undefined};
    }

    function addMenuSettings() {
        $.contextMenu({
            selector: '.container-setting',
            callback: function (key, options) {
                //var m = "clicked: " + key;
                //var info = options.$trigger.attr("info");
                switch (key) {
                    case "edit":
                        var dcblock = $(this).parent(); // берем родителя элемента на котором кликнули
                        var infoElem = $("span.block-id", dcblock)[0]; // находим элемент с информацией о блоке
                        var dcId = infoElem.getAttribute("data-cid"); // берем из него информацию об ид контейнера
                        currData.blockName = infoElem.getAttribute("data-name"); // и имени блока

                        $.when(ajaxDinamicContent(dcId), ajaxNamesContainers(dcId)).done(function (dc, nc) {
                            var dyncon = dc[0], names = nc[0], block;
                            currData.dyncon = dyncon;
                            currData.names = names;
                            var positionInForm = $(selectors.positionAddContent).empty();
                            $("#nameblock").text(dyncon.name);
                            $("#idblock").text(dyncon.id);
                            $("#idsname").text(dyncon.shablonName ? dyncon.shablonName : "по умолчанию");

                            var child = getTemplateAsString(selectors.tmpFirst); // получаем шаблок как строку, иначе замена не сработает
                            block = getInfoAbautContainers(dyncon, currData.blockName).data; // получаем инфо о контейнере
                            var contentTemplate = useTemplate(child, {id: dyncon.id, name: currData.blockName, num: 1}); // добавляем переменные в шаблон
                            positionInForm.append(contentTemplate); // добавляем к диалогу
                            // теперь шаблон доступен и можнос ним работать
                            showSelectorContainers(selectors.allcont);
                            $(selectors.allcont).change();
                            $(selectors.shName).change();

                            if (typeof CKEDITOR !== "undefined") {
                                currData.editor = CKEDITOR.replace(selectors.editor);
                            }

                            var inputel = $("#" + dyncon.id + "-" + currData.blockName + "-inp");
                            var selel = $("#" + dyncon.id + "-" + currData.blockName + "-sel");

                            selel.change(function () {
                                var block = getInfoAbautContainers(dyncon, currData.blockName); // олучаем инфо о контейнере
                                block.content = "";
                                var type = $(this).val();
                                inputel.removeClass("hidden");
                                inputel.removeClass("invisible");
                                $(selectors.allcont).addClass("hidden");
                                $(selectors.framecont).addClass("hidden");
                                $(selectors.razmetka).addClass("hidden");
                                $(selectors.wrapeditor).addClass("hidden");
                                inputel.val("");// очистка значения
                                if (block.data && currData.oldValue[block.data.type] && type === currData.oldValue[block.data.type].type) {
                                    inputel.val(currData.oldValue[block.data.type].content); //  установка старого
                                }
                                if (type === "container") {
                                    inputel.addClass("hidden");
                                    $(selectors.allcont).removeClass("hidden");
                                    $(selectors.framecont).removeClass("hidden");
                                    $(selectors.wrapeditor).addClass("hidden");
                                }
                                if (type === "text") {
                                    inputel.addClass("invisible");
                                    $(selectors.wrapeditor).removeClass("hidden");
                                }
                            });//

                            if (block) { // устанавливаем значения в поля
                                selel.val(block.type);//
                                inputel.val(block.content);//
                                if ("container" === block.type) {
                                    $(selectors.allcont).val(block.content);//
                                }
                                if ("text" === block.type) {
                                    currData.editor.setData(block.content);
                                }
                                currData.oldValue[block.type] = block; // запоминаем старые данные для удобства
                            }
                            selel.change();

                            // далее код для отображения всех блоков - не ломал, думаю пригодится. вместо него выше для конкретного - редактируемого
                            //for (var i = 0; i < names.length; i++) {
                            //    if (names[i].includes("head")) {
                            //        log("Это блок для скриптов - даже не отображаем его");
                            //        continue;
                            //    }
                            //    needOpen = true;
                            //
                            //    var child = getTemplateAsString("#tmpFirst"); // получаем шаблок как строку, иначе замена не сработает
                            //    block = getInfoAbautContainers(dyncon, names[i]).data;
                            //    var contentTemplate = useTemplate(child, {id: dyncon.id, name: names[i], num: (i + 1)}); // добавляем переменные в шаблон
                            //    $("#dialog-form-add-content").append(contentTemplate); // добавляем к диалогу
                            //    if (block) {
                            //        $("#" + dyncon.id + "-" + names[i] + "-sel").val(block.type);//
                            //        $("#" + dyncon.id + "-" + names[i] + "-inp").val(block.content);//
                            //    }
                            //
                            //}


                            dialogAdd.dialog("open"); // открытие диалогового окна
                        }).fail(handleAjaxError);

                        break;
                    default:

                        break;
                }
            }
            ,
            items: menuData.items
        })
    }

    function addEditIconToBlocks() {
        $('div.block-empty').prepend("<span class='container-setting ui-icon ui-icon-wrench'>ПУСТОЙ</span>");
        $('div.block-notempty').prepend("<span class='container-setting ui-icon ui-icon-wrench'>НЕПУСТОЙ</span>");
        addMenuSettings();
    }

    function podsvetka() {
        //$("#backlight").text("Для снятия выделения перегрузите страницу");
        $('div.block-empty').toggleClass("select-empty");
        $('div.block-notempty').toggleClass("select-notempty");
        $('div.block-center').addClass("select-center");
        $('div>span.ui-icon-wrench').toggleClass("hidden");
        //$('.block-id').toggleClass("hidden");
        //$("[info='test2']").attr("random",getRandomInt(1,100));
    }

    function prepareSendData() {
        saveData();
    }

    function saveData(ndc) {
        var block, type, dataBlock, name;
        var formData;
        //= {
        //    'name': $('input[name=name]').val(),
        //    'email': $('input[name=email]').val(),
        //    'superheroAlias': $('input[name=superheroAlias]').val()
        //};
        if (!currData.dyncon.cont) {
            currData.dyncon.cont = [];
        }
        formData = currData.dyncon;
        //for (var i = 0; i < currData.names.length; i++) {
        //    block = getInfoAbautContainers(currData.dyncon, currData.names[i]);
        //    name = currData.names[i]; // имя блока
        //    type = $("#" + currData.dyncon.id + "-" + name + "-sel").val();// его тип
        //    dataBlock = $("#" + currData.dyncon.id + "-" + name + "-inp").val();// его данные
        //    position = block.position ? block.position : position++;
        //    if (dataBlock) {
        //        formData.cont[position] = {name: name, type: type, content: dataBlock};
        //    } else {
        //        formData.cont[position] = null;
        //    }
        //}
        block = getInfoAbautContainers(currData.dyncon, currData.blockName);
        name = currData.blockName; // имя блока
        type = $("#" + currData.dyncon.id + "-" + name + "-sel").val();// его тип
        dataBlock = $("#" + currData.dyncon.id + "-" + name + "-inp").val();// его данные
        if (type === "container") {
            dataBlock = $(selectors.allcont).val();// данные для контейнера
            if (dataBlock === "empty") {
                dataBlock = "";
            }
            if (typeof ndc === "undefined" && dataBlock === "new") {
                var shablon = $(selectors.shName).val();
                var nameDC = $(selectors.nameDC).val();
                currData.ndc = createNewContainerFromShablon(nameDC, shablon, "container");
                ajaxCreateDynContent(currData.ndc, saveData); // callback вызов этой же функцийй после аякса
                return;
            }
            if (typeof ndc !== "undefined" && dataBlock === "new") {
                // пришел аякс с данными о созданном DC
                dataBlock = ndc.id;
            }


        }
        if (type === "text") {
            dataBlock = currData.editor.getData();// данные из редактора
        }
        if (dataBlock) {
            var p;
            if (block.position != null) {
                p = block.position;
            } else {
                p = formData.cont.length;
            }
            formData.cont[p] = {name: name, type: type, content: dataBlock};
        } else {
            formData.cont[block.position] = {name: name, type: "remove", content: null}; // будет удален так как content is null
        }

        log("save : %s", JSON.stringify(formData));

        $.when(ajaxSaveDynContent(formData))
            .done(function (doneSaveData) {
                if (doneSaveData) {
                    dialogAdd.dialog("close");
                    location.reload();
                }
            }).fail(handleAjaxError);
    }

    function createNewContainerFromShablon(name, shablon, typeContainer) {
        var dc = {};
        dc.id = 0;
        dc.clone = false;
        dc.cont = [];
        dc.name = name ? name : "created dynamically";
        dc.pastRoot = false;
        //dc.redirectUrl=[];
        dc.shablonName = shablon;
        //dc.type = "dynamicContent";
        dc.typeDynCont = typeContainer;
        return dc
    }

    // плагин уведомлений
    function generate(text, type, timeout) {
        var n = noty({
            text: text,
            type: type, // var typeAlerts as enum
            dismissQueue: true,
            layout: 'topCenter',
            timeout: timeout ? timeout : false,
            theme: 'bootstrapTheme',
            closeWith: ['button', 'click'],
            maxVisible: 20,
            modal: false

        });
        //console.log('html: ' + n.options.id);
    }

    function generateWithButton(text, type, funClickYes) {
        var n = noty({
            text: text,
            type: type, // var typeAlerts as enum
            dismissQueue: true,
            layout: 'center',
            theme: 'defaultTheme',
            closeWith: ['button', 'click'],
            maxVisible: 20,
            modal: false,
            buttons: [
                {
                    addClass: 'btn btn-success', text: 'Да', onClick: function ($noty) {
                    $noty.close();
                    funClickYes();
                }
                },
                {
                    addClass: 'btn btn-default', text: 'Cancel',
                    onClick: function ($noty) {
                        $noty.close();
                    }
                }
            ]
        });
        //console.log('html: ' + n.options.id);
    }


    // default name module
    window.adminMenu = new Api();
    return adminMenu;
}(jQuery));




