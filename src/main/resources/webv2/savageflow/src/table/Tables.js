import { useEffect, useState } from 'react';
import TabMenu from '../common/TabMenu';
import DataTable from './DataTable';
import TableService from './TableService';



const Tables = (props) => {
  const [tables, setTables] = useState([])
  const [table, setTable] = useState(null)

  useEffect(() => new TableService().getTables((t) => {
    setTables(t)
    setTable(t[0])
  }, console.log), [])


  return (
    <>
      <TabMenu tabs={tables} onChange={setTable}>
        <DataTable table={table}/>
      </TabMenu>
    </>
  )
}

export default Tables