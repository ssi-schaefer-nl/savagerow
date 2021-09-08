import HomeIcon from '@material-ui/icons/Home';
import WorkIcon from '@material-ui/icons/Work';
import Workflows from '../workflow/Workflows';

const Routes = [
    {
        link: "/",
        name: "Home",
        exact: true,
        icon: HomeIcon
    },
    {
        link: "/workflows",
        name: "Workflows",
        icon: WorkIcon,
        content: <Workflows/>,
    }
]

export default Routes