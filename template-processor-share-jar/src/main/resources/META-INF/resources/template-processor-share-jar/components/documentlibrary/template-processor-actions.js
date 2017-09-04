(function () {
    YAHOO.Bubbling.fire('registerAction', {
        actionName: 'onActionProceedTemplate',
        fn: function (file) {
            this.widgets.waitDialog = Alfresco.util.PopupManager.displayMessage({
                text: this.msg('com.aimprosoft.templateProcessor.action.ProceedTemplate.msg.processing'),
                spanClass: 'wait',
                displayTime: 0
            });
            var popup = this.widgets.waitDialog;
            this.modules.actions.genericAction({
                success: {
                    callback: {
                        fn: function () {
                            popup.destroy();
                            YAHOO.Bubbling.fire('metadataRefresh');
                            YAHOO.Bubbling.fire('previewChangedEvent');
                        },
                        scope: this
                    }
                },
                failure: {
                    callback: {
                        fn: function (response) {
                            popup.destroy();
                            Alfresco.util.PopupManager.displayPrompt({
                                title: this.msg('com.aimprosoft.templateProcessor.action.ProceedTemplate.msg.error'),
                                text: response.json.message,
                                buttons: [{
                                    text: this.msg('button.ok'),
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
                    name: '/aim/template-processor/fill-document?nodeRef={nodeRef}',
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