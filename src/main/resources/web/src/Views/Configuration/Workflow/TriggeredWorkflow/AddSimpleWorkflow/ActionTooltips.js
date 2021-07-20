const ActionTooltips = {
    Trigger_Other_Workflows: () => "When enabled, this action can trigger the execution of other workflows. Use this with caution, as it might trigger a never-ending cycle of workflow actions.",
    Row: (action) => "This is the table where this action will " + action + " a row",
    RightClick: (type) => "By right clicking the " + type + " fields you can access placeholders for the fields of the row that triggered this workflow. Note that for updating these fields contain the values after the update, and for deletion these fields contain the values of the deleted row.",
    TriggeredRow: (action) => "By enabling this option you will " + action + " the row that triggered this workflow. That means that the table and the criteria for the row are already set for you.",

}

export default ActionTooltips