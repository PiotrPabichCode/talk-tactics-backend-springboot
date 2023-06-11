import * as React from 'react';
import Paper from '@mui/material/Paper';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TablePagination from '@mui/material/TablePagination';
import TableRow from '@mui/material/TableRow';
import useLoadCourses from '../admin/courses/hooks/useLoadCourses';
import { Box, Button, TableSortLabel } from '@mui/material';
import { Add } from '@mui/icons-material';
import { visuallyHidden } from '@mui/utils';
import { getUsername, request } from '../../api/AxiosHelper';
import { toast } from 'react-toastify';

const columns = [
  { id: 'id', label: 'ID', minWidth: 50 },
  { id: 'name', label: 'Name', minWidth: 250 },
  { id: 'description', label: 'Description', minWidth: 250 },
  { id: 'level', label: 'Level', minWidth: 170 },
  { id: 'add', label: 'Add course', minWidth: 150 },
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

export default function Courses() {
  const [courses, setCourses] = useLoadCourses();

  const [page, setPage] = React.useState(0);
  const [rowsPerPage, setRowsPerPage] = React.useState(10);

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(+event.target.value);
    setPage(0);
  };

  const handleAddCourse = async (event, id) => {
    event.preventDefault();
    try {
      const selectedCourse = courses.find((course) => course.id === id);

      const requestData = {
        courseName: selectedCourse.name,
        login: getUsername(),
      };
      await request('PUT', `/api/user-courses`, requestData);
      toast.success('Course added successfully');
    } catch (error) {
      console.log(error);
      toast.error('You already have this course');
    }
  };

  return (
    <div className='d-flex justify-content-center m-5'>
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
            <TableBody>
              {courses.length > 0 &&
                courses
                  .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
                  .map((course, index) => {
                    return (
                      <TableRow
                        hover
                        role='checkbox'
                        tabIndex={-1}
                        key={course.id}>
                        <TableCell>
                          {index + 1 + (page * rowsPerPage, page * rowsPerPage)}
                        </TableCell>
                        <TableCell>{course.name}</TableCell>
                        <TableCell>{course.description}</TableCell>
                        <TableCell>{course.level}</TableCell>
                        <TableCell>
                          <Button
                            size='small'
                            variant='contained'
                            endIcon={<Add />}
                            onClick={(e) => handleAddCourse(e, course.id)}>
                            Add
                          </Button>
                        </TableCell>
                      </TableRow>
                    );
                  })}
            </TableBody>
          </Table>
        </TableContainer>
        <TablePagination
          rowsPerPageOptions={[5, 10, 25]}
          component='div'
          count={courses.length}
          rowsPerPage={rowsPerPage}
          page={page}
          onPageChange={handleChangePage}
          onRowsPerPageChange={handleChangeRowsPerPage}
        />
      </Paper>
    </div>
  );
}
