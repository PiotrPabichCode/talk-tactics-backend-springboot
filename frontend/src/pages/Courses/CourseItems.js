import * as React from 'react';
import Paper from '@mui/material/Paper';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TablePagination from '@mui/material/TablePagination';
import TableRow from '@mui/material/TableRow';
import {
  Box,
  Button,
  Card,
  CardContent,
  Checkbox,
  TableSortLabel,
} from '@mui/material';
import ReadMoreIcon from '@mui/icons-material/ReadMore';
import { visuallyHidden } from '@mui/utils';
import { getUserRole, getUsername, request } from '../../api/AxiosHelper';
import { Link, useParams, useNavigate } from 'react-router-dom';
import { CheckBox, Delete } from '@mui/icons-material';
import { toast } from 'react-toastify';
import deleteCourseItem from './hooks/deleteCourseItem';

const columns = [
  { id: 'id', label: 'ID', minWidth: 50 },
  { id: 'word', label: 'Word', minWidth: 250 },
  { id: 'course', label: 'Course', minWidth: 250 },
  { id: 'action', label: 'Action', minWidth: 1 },
];

function descendingComparator(a, b, orderBy) {
  if (b[orderBy] < a[orderBy]) {
    return -1;
  }
  if (b[orderBy] > a[orderBy]) {
    return 1;
  }
  return 0;
}

function getComparator(order, orderBy) {
  return order === 'desc'
    ? (a, b) => descendingComparator(a, b, orderBy)
    : (a, b) => -descendingComparator(a, b, orderBy);
}

function stableSort(array, comparator) {
  const stabilizedThis = array.map((el, index) => [el, index]);
  stabilizedThis.sort((a, b) => {
    const order = comparator(a[0], b[0]);
    if (order !== 0) {
      return order;
    }
    return a[1] - b[1];
  });
  return stabilizedThis.map((el) => el[0]);
}

export default function CourseItems() {
  const { courseID, userID } = useParams();
  const navigate = useNavigate();
  const [courseItems, setCourseItems] = React.useState([]);
  const [userCourseItems, setUserCourseItems] = React.useState([]);
  const handleCheckboxChange = async (id) => {
    try {
      request('POST', `/api/user-course-items/${id}/isLearned`);
      const updatedUserCourseItems = [...userCourseItems];
      const index = updatedUserCourseItems.findIndex(
        (courseItem) => courseItem.id === id
      );
      updatedUserCourseItems[index].learned =
        !updatedUserCourseItems[index].learned;
      setUserCourseItems(updatedUserCourseItems);
    } catch (error) {
      console.log(error);
    }
  };

  React.useEffect(() => {
    const loadUserCourseItems = async () => {
      try {
        if (userID) {
          const response = await request(
            'GET',
            `/api/user-courses/${courseID}`
          );
          setUserCourseItems(response.data.userCourseItems);
        } else if (courseID) {
          const response = await request(
            'GET',
            `/api/courses/${courseID}/course-items`
          );
          setCourseItems(response.data);
        } else {
          const response = await request('GET', `/api/course-items`);
          setCourseItems(response.data);
        }
      } catch (error) {
        console.log(error);
      }
    };
    loadUserCourseItems();
  }, [courseID, userID]);

  const [page, setPage] = React.useState(0);
  const [rowsPerPage, setRowsPerPage] = React.useState(25);

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(+event.target.value);
    setPage(0);
  };

  const handleDeleteAction = async (e, id) => {
    try {
      await deleteCourseItem(id);
      setCourseItems((prevCourses) => {
        return prevCourses.filter((course) => course.id !== id);
      });
    } catch (error) {
      console.log(error);
      toast.error('Something went wrong');
    }
  };

  const renderUserCourseItems = () => {
    return (
      <TableBody>
        {userCourseItems.length > 0 &&
          userCourseItems
            .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
            .map((item, index) => {
              return (
                <TableRow hover role='checkbox' tabIndex={-1} key={item.id}>
                  <TableCell>
                    {index + 1 + (page * rowsPerPage, page * rowsPerPage)}
                  </TableCell>
                  <TableCell>{item.courseItem.word}</TableCell>
                  <TableCell>{item.courseItem.course.name}</TableCell>
                  <TableCell>
                    <Button
                      size='small'
                      variant='outlined'
                      component={Link}
                      to={`/courses/${item.courseItem.course.id}/items/${item.courseItem.id}`}
                      endIcon={<ReadMoreIcon />}>
                      Show details
                    </Button>
                    <Checkbox
                      checked={item.learned}
                      onChange={() => handleCheckboxChange(item.id)}
                      inputProps={{ 'aria-label': 'controlled' }}
                    />
                  </TableCell>
                </TableRow>
              );
            })}
      </TableBody>
    );
  };

  const renderCourseItems = () => {
    return (
      <TableBody>
        {courseItems.length > 0 &&
          courseItems
            .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
            .map((item, index) => {
              return (
                <TableRow hover role='checkbox' tabIndex={-1} key={item.id}>
                  <TableCell>
                    {index + 1 + (page * rowsPerPage, page * rowsPerPage)}
                  </TableCell>
                  <TableCell>{item.word}</TableCell>
                  <TableCell>{item.course.name}</TableCell>
                  <TableCell>
                    <Button
                      size='small'
                      variant='outlined'
                      component={Link}
                      to={`/courses/${item.course.id}/items/${item.id}`}
                      endIcon={<ReadMoreIcon />}>
                      Show details
                    </Button>
                    {getUserRole() === 'ADMIN' && (
                      <Button
                        sx={{ marginLeft: '10px' }}
                        size='small'
                        variant='contained'
                        endIcon={<Delete />}
                        onClick={(e) => handleDeleteAction(e, item.id)}>
                        Delete
                      </Button>
                    )}
                  </TableCell>
                </TableRow>
              );
            })}
      </TableBody>
    );
  };

  return (
    <div className='d-flex justify-content-center m-5'>
      <Card>
        <div style={{ display: 'flex', justifyContent: 'flex-end' }}>
          <button className='btn btn-primary m-2' onClick={() => navigate(-1)}>
            Back
          </button>
        </div>
        <CardContent>
          <Paper sx={{ width: '100%', overflow: 'hidden' }}>
            <TableContainer sx={{ maxHeight: 440 }}>
              <Table stickyHeader aria-label='sticky table'>
                <TableHead>
                  <TableRow>
                    {columns.map((column) => (
                      <TableCell
                        key={column.id}
                        align={column.align}
                        style={{ minWidth: column.minWidth }}>
                        <TableSortLabel>{column.label}</TableSortLabel>
                      </TableCell>
                    ))}
                  </TableRow>
                </TableHead>
                {userCourseItems.length > 0
                  ? renderUserCourseItems()
                  : renderCourseItems()}
              </Table>
            </TableContainer>
            <TablePagination
              rowsPerPageOptions={[5, 25, 100]}
              component='div'
              count={
                userCourseItems.length > 0
                  ? userCourseItems.length
                  : courseItems.length
              }
              rowsPerPage={rowsPerPage}
              page={page}
              onPageChange={handleChangePage}
              onRowsPerPageChange={handleChangeRowsPerPage}
            />
          </Paper>
        </CardContent>
      </Card>
    </div>
  );
}
