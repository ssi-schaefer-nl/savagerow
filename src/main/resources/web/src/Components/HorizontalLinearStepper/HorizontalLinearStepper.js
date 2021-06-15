import React, { useState } from "react";
import { makeStyles } from '@material-ui/core/styles';
import Stepper from '@material-ui/core/Stepper';
import Step from '@material-ui/core/Step';
import StepLabel from '@material-ui/core/StepLabel';
import Button from '@material-ui/core/Button';
import { Grid } from "@material-ui/core";


const useStyles = makeStyles((theme) => ({
    root: {
        width: '100%',
    },
    button: {
        marginRight: theme.spacing(1),
    },
}));


export default function HorizontalLinearStepper(props) {
    const { steps, onFinish } = props;
    const classes = useStyles();
    const [activeStep, setActiveStep] = React.useState(0);


    const handleNext = () => {
        const currentStep = steps[activeStep]
        if(currentStep.onNext != undefined)
             currentStep.onNext()
        if(activeStep != steps.length - 1 ) 
            setActiveStep((prevActiveStep) => prevActiveStep + 1);
    };

    const handleBack = () => {
        setActiveStep((prevActiveStep) => prevActiveStep - 1);
    };


    return (
        <div className={classes.root}>
            <Stepper activeStep={activeStep}>
                {steps.map((step, index) => {
                    const stepProps = {};
                    const labelProps = {};
                    return (
                        <Step key={step.name} {...stepProps}>
                            <StepLabel {...labelProps}>{step.name}</StepLabel>
                        </Step>
                    );
                })}
            </Stepper>

            <Grid direction="column" style={{ margin: "0 2em 0 2em" }}>
                <Grid item style={{ minHeight: "20vh" }}>
                    {steps[activeStep].Component}
                </Grid>
                <Grid item>
                    <Button disabled={(activeStep === 0) || steps[activeStep].restrictBack} onClick={handleBack} className={classes.button}>Back</Button>
                    <Button
                        variant="contained" color="primary"
                        onClick={handleNext}
                        className={classes.button}
                        disabled={steps[activeStep].nextAllowed != undefined ? !steps[activeStep].nextAllowed : false}
                    >
                        {steps[activeStep].nextButton != undefined ? steps[activeStep].nextButton : "Next"}
                    </Button>


                </Grid>
            </Grid>

        </div>
    );
}