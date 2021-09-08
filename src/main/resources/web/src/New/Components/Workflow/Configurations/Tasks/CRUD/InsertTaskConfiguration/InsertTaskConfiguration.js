import React from "react";
import HorizontalLinearStepper from "../../../../HorizontalLinearStepper/HorizontalLinearStepper";

const InsertTaskConfiguration = () => {
    
    const steps = [
        {
            "name": "Define Input Parameters",
            "Component": <p>Test</p>,
        },
        {
            "name": "Define Insert Action",
            "Component": <p>Test</p>,
        },
        {
            "name": "Define Output Parameters",
            "Component": <p>Test</p>,
        },
    ]

    return (
        <div style={{ height: "60vh", width: "70vw" }}>

            <HorizontalLinearStepper steps={steps} />
        </div>

    );
}

export default InsertTaskConfiguration;
