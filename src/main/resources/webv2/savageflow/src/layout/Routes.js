import HomeIcon from '@material-ui/icons/Home';
import WorkIcon from '@material-ui/icons/Work';
import Workflows from '../workflow/Workflows';
import TableChartIcon from '@material-ui/icons/TableChart';
import Tables from '../table/Tables';

const Routes = [
    {
        link: "/",
        name: "Home",
        exact: true,
        icon: HomeIcon
    },
    {
        link: "/tables",
        name: "Tables",
        icon: TableChartIcon,
        content: <Tables/>
    },
    {
        link: "/workflows",
        name: "Workflows",
        icon: WorkIcon,
        content: <Workflows />,
    }
]

export default Routes