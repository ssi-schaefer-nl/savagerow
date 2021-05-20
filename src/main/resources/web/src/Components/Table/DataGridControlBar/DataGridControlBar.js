import React from 'react';
import { grey } from '@material-ui/core/colors';

import MultiSelect from "../../MultiSelect/MultiSelect";
import { Box, Grid } from "@material-ui/core";
import Accordion from '@material-ui/core/Accordion';
import AccordionSummary from '@material-ui/core/AccordionSummary';
import AccordionDetails from '@material-ui/core/AccordionDetails';
import Typography from '@material-ui/core/Typography';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';


const DataGridControlBar = (props) => {
  const { columns, onChangeColumnFilter } = props

  return (
    <Box style={{ padding: "0.5em 1em 2em 1em", borderBottom: "1px solid grey", backgroundColor: grey[50] }}>

      <Grid container direction="row">
        <Grid container >
          <Grid item xs={2} >
            <MultiSelect
              options={columns.map(col => col.column)}
              onChange={onChangeColumnFilter}
              placeholder="Column Filters"
            />
          </Grid>
        </Grid>
      </Grid>
    </Box>


  )
}

export default DataGridControlBar