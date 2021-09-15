import { AppBar, Grid } from '@material-ui/core';
import { makeStyles, withStyles } from '@material-ui/core/styles';
import Tab from '@material-ui/core/Tab';
import Tabs from '@material-ui/core/Tabs';
import React from 'react';

const AntTabs = withStyles({
    indicator: {
        backgroundColor: '#1890ff',
    },
    root: {
        
    }
})(Tabs);

const useStyles = makeStyles((theme) => ({
    root: {
        flexGrow: 1,
    },
    demo1: {
        backgroundColor: theme.palette.background.paper,
    }
}));

export default function TabMenu({ tabs, children, onChange }) {
    const classes = useStyles();
    const [value, setValue] = React.useState(0);

    const handleChange = (event, newValue) => {
        setValue(newValue)
        onChange(tabs[newValue])

    };

    return (
        <>
            <AntTabs value={value} onChange={handleChange} variant="scrollable" scrollButtons="auto" >
                {tabs.map((t, i) => <Tab label={t} {...a11yProps({ i })} />)}
            </AntTabs>

            <div style={{ height: '50vh', marginTop: "1em" }}>
                {children}
            </div>
        </>
    );
}


function a11yProps(index) {
    return {
        id: `simple-tab-${index}`,
        'aria-controls': `simple-tabpanel-${index}`,
    };
}
