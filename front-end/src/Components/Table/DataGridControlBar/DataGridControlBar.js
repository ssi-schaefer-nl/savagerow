import React from 'react';
import { grey } from '@material-ui/core/colors';

import MultiSelect from "../../MultiSelect/MultiSelect";
import { Grid } from "@material-ui/core";
import Accordion from '@material-ui/core/Accordion';
import AccordionSummary from '@material-ui/core/AccordionSummary';
import AccordionDetails from '@material-ui/core/AccordionDetails';
import Typography from '@material-ui/core/Typography';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';


const DataGridControlBar = (props) => {
  const { columns, onChangeColumnFilter } = props

  return (
    <Accordion>
      <AccordionSummary
        style={{ backgroundColor: grey[100], border: "1px solid", borderColor: grey[300] }}
        expandIcon={<ExpandMoreIcon />}
        aria-controls="panel1a-content"
        id="panel1a-header"
      >
        <Typography variant="button" >Table Controls</Typography>
      </AccordionSummary >
      <AccordionDetails style={{ backgroundColor: grey[50], border: "1px solid transparent", borderColor: grey[300]  }}>

        <Grid container direction="row">
          <Grid container >
            <Grid item xs={2} style={{ paddingBottom: "0.5em" }}>
              <MultiSelect
                options={columns.map(col => col.column)}
                onChange={onChangeColumnFilter}
                placeholder="Column Filters"
              />
            </Grid>
          </Grid>
        </Grid>

      </AccordionDetails>
    </Accordion>


  )
}

export default DataGridControlBar