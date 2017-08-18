(function () {
    YAHOO.Bubbling.fire("registerAction", {
        actionName: "onActionProceedFillValues",
        fn: function (file) {
            this.widgets.waitDialog = Alfresco.util.PopupManager.displayMessage({
                text: this.msg("aim.template.action.ActionProceedFillValues.msg.proceed.msg"),
                spanClass: "wait",
                displayTime: 0
            });
            var popup = this.widgets.waitDialog;
            this.modules.actions.genericAction({
                success: {
                    callback: {
                        fn: function (response) {
                            debugger;
                            popup.destroy();
                            Alfresco.util.PopupManager.displayPrompt({
                                title: this.msg("aim.template.action.ActionProceedFillValues.msg.success"),
                                text: response.json.message,
                                buttons: [{
                                    text: this.msg("button.ok"),
                                    handler: function () {
                                        this.destroy();
                                        YAHOO.Bubbling.fire("metadataRefresh");
                                        YAHOO.Bubbling.fire("previewChangedEvent");
                                    },
                                    isDefault: true
                                }]
                            });
                        },
                        scope: this
                    }
                },
                failure: {
                    callback: {
                        fn: function (response) {
                            popup.destroy();
                            Alfresco.util.PopupManager.displayPrompt({
                                title: this.msg("aim.template.action.ActionProceedFillValues.msg.failure"),
                                text: response.json.message,
                                buttons: [{
                                    text: this.msg("button.ok"),
                                    handler: function () {
                                        this.destroy();
                                    },
                                    isDefault: true
                                }]
                            });
                        },
                        scope: this
                    }
                },
                webscript: {
                    name: "/aim/template-processor/fill-values?nodeRef={nodeRef}",
                    stem: Alfresco.constants.PROXY_URI,
                    method: Alfresco.util.Ajax.GET,
                    params: {
                        nodeRef: file.nodeRef
                    }
                },
                config: {}
            });
        }
    });
})();