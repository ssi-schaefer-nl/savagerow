import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Stepper from '@material-ui/core/Stepper';
import Step from '@material-ui/core/Step';
import StepLabel from '@material-ui/core/StepLabel';
import StepContent from '@material-ui/core/StepContent';
import Button from '@material-ui/core/Button';

const useStyles = makeStyles((theme) => ({
    root: {
        width: '100%',
    },
    button: {
        marginTop: theme.spacing(1),
        marginRight: theme.spacing(1),
    },
    actionsContainer: {
        marginBottom: theme.spacing(2),
    },
    resetContainer: {
        padding: theme.spacing(3),
    },
}));



export default function VerticalLinearStepper(props) {
    const { steps: allSteps, onFinish } = props;

    const steps = allSteps.filter(s => s.disabled == undefined || !s.disabled)
    const classes = useStyles();
    const [activeStep, setActiveStep] = React.useState(0);

    const handleNext = () => {
        const currentStep = steps[activeStep]

        if (currentStep.onNext != undefined)
            currentStep.onNext()
        if (activeStep != steps.filter(s => !s.disabled).length - 1)
            setActiveStep((prevActiveStep) => prevActiveStep + 1);
    };

    const handleBack = () => {
        setActiveStep((prevActiveStep) => prevActiveStep - 1);
    };


    return (
        <div className={classes.root}>
            <Stepper activeStep={activeStep} orientation="vertical">
                {steps.filter(s => s.disabled == undefined || !s.disabled).map((step, index) => (
                    <Step key={step.name}>
                        <StepLabel>{step.name}</StepLabel>
                        <StepContent>
                            <div style={{ margin: "1em" }}>
                                {step.Component}
                            </div>
                            <div className={classes.actionsContainer}>
                                <div>
                                    <Button disabled={(activeStep === 0) || step.restrictBack} onClick={handleBack} className={classes.button}>Back</Button>
                                    <Button
                                        variant="outlined"
                                        color="primary"
                                        type={step.nextButtonType}
                                        onClick={handleNext}
                                        className={classes.button}
                                        disabled={step.nextAllowed != undefined ? !step.nextAllowed : false}
                                    >
                                        {step.nextButton != undefined ? step.nextButton : "Next"}
                                    </Button>
                                </div>
                            </div>
                        </StepContent>
                    </Step>
                ))}
            </Stepper>
        </div>
    );
}
