import React from "react";
import HorizontalLinearStepper from "../../../HorizontalLinearStepper/HorizontalLinearStepper";
import PopupWindow from "../../../PopupWindow/PopupWindow";


const StartElement = ({ open, elementId, onClose }) => {
    const editingSteps = [
        {
            "name": "Test",
            "Component": <p>Test</p>,
        },
        {
            "name": "Test",
            "Component": <p>Test</p>,
        },
    ]

    return (
        <PopupWindow open={open} onClose={onClose} title={"Test"} wide>
            <div style={{ height: "60vh", width: "70vw" }}>
                <HorizontalLinearStepper steps={editingSteps} onFinish />
            </div>
        </PopupWindow>

    );
}

export default StartElement;
