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
    const { steps } = props;
    const classes = useStyles();
    const [activeStep, setActiveStep] = React.useState(0);


    const handleNext = () => {
        setActiveStep((prevActiveStep) => prevActiveStep + 1);
    };

    const handleBack = () => {
        setActiveStep((prevActiveStep) => prevActiveStep - 1);
    };

    const handleFinish = () => {
        console.log("finished")
    }

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
                    <Button disabled={activeStep === 0} onClick={handleBack} className={classes.button}>Back</Button>
                    {activeStep === steps.length - 1
                        ?
                        <Button variant="contained" color="primary" onClick={handleFinish} className={classes.button}>Finish</Button>
                        :
                        <Button variant="contained" color="primary" onClick={handleNext} className={classes.button}>Next</Button>
                    }

                </Grid>
            </Grid>

        </div>
    );
}