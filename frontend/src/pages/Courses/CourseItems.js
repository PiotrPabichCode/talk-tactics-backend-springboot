import * as React from 'react';
import Paper from '@mui/material/Paper';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TablePagination from '@mui/material/TablePagination';
import TableRow from '@mui/material/TableRow';
import { Box, Button, TableSortLabel } from '@mui/material';
import ReadMoreIcon from '@mui/icons-material/ReadMore';
import { visuallyHidden } from '@mui/utils';
import { getUsername, request } from '../../api/AxiosHelper';
import { toast } from 'react-toastify';
import { Link } from 'react-router-dom';

const columns = [
  { id: 'id', label: 'ID', minWidth: 50 },
  { id: 'word', label: 'Word', minWidth: 250 },
  { id: 'course', label: 'Course', minWidth: 250 },
  { id: 'action', label: 'Action', minWidth: 170 },
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
  const [courseItems, setCourseItems] = React.useState([]);
  React.useEffect(() => {
    const loadCourseItems = async () => {
      try {
        const response = await request('GET', '/api/course-items');
        setCourseItems(response.data);
      } catch (error) {
        console.log(error);
      }
    };
    loadCourseItems();
  }, []);

  const [page, setPage] = React.useState(0);
  const [rowsPerPage, setRowsPerPage] = React.useState(10);

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(+event.target.value);
    setPage(0);
  };

  return (
    <div className='d-flex justify-content-center m-5'>
      <Paper alignCenter sx={{ width: '100%', overflow: 'hidden' }}>
        <TableContainer>
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
            <TableBody>
              {courseItems &&
                courseItems
                  .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                  .map((item, index) => {
                    return (
                      <TableRow
                        hover
                        role='checkbox'
                        tabIndex={-1}
                        key={item.id}>
                        <TableCell key={item.id}>
                          {index + 1 + (page * rowsPerPage, page * rowsPerPage)}
                        </TableCell>
                        <TableCell key={item.id}>{item.word}</TableCell>
                        <TableCell key={item.id}>{item.course.name}</TableCell>
                        <TableCell>
                          <Button
                            size='small'
                            variant='contained'
                            endIcon={<ReadMoreIcon />}
                            component={Link}
                            to={`/courseItemDetails/${item.id}`}>
                            Show details
                          </Button>
                        </TableCell>
                      </TableRow>
                    );
                  })}
            </TableBody>
          </Table>
        </TableContainer>
        <TablePagination
          rowsPerPageOptions={[5, 25, 100]}
          component='div'
          count={courseItems.length}
          rowsPerPage={rowsPerPage}
          page={page}
          onPageChange={handleChangePage}
          onRowsPerPageChange={handleChangeRowsPerPage}
        />
      </Paper>
    </div>
  );
}
