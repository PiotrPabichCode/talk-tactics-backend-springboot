import React from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { useCourseDetails } from './hooks/useCourseDetails';
import useLoadCourseItems from './hooks/useLoadCourseItems';
import {
  Button,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TablePagination,
  TableRow,
  TableSortLabel,
} from '@mui/material';
import ReadMoreIcon from '@mui/icons-material/ReadMore';
import { useTranslation } from 'react-i18next';

export default function ViewCourse() {
  const { t } = useTranslation();
  const url = '/admin?isCoursesDisplayed=true';
  const { id } = useParams();
  const courseDetails = useCourseDetails(id);
  const [courseItems, setCourseItems] = useLoadCourseItems(id);
  const navigate = useNavigate();

  const columns = [
    { id: 'id', label: 'ID', minWidth: 50 },
    { id: 'word', label: t('admin.courses.view_course.word'), minWidth: 250 },
    {
      id: 'course',
      label: t('admin.courses.view_course.course'),
      minWidth: 250,
    },
    {
      id: 'action',
      label: t('admin.courses.view_course.action'),
      minWidth: 200,
    },
  ];

  const [page, setPage] = React.useState(0);
  const [rowsPerPage, setRowsPerPage] = React.useState(25);

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(+event.target.value);
    setPage(0);
  };

  return (
    <div className='container-fluid p-4'>
      <div className='container-fluid rounded p-4 shadow bg-dark position-relative'>
        <button
          className='btn btn-primary position-absolute end-0 me-4'
          onClick={() =>
            navigate(-1, {
              isCoursesDisplayed: true,
            })
          }>
          {t('admin.courses.view_course.back')}
        </button>
        <h2 className='text-center m-4 text-light'>
          {t('admin.courses.view_course.title')}
        </h2>
        <div className='card mb-4'>
          <div className='card-header'>
            {t('admin.courses.view_course.title')}
          </div>
          <div className='card-body'>
            <div className='row'>
              <div className='col-sm-3'>
                <p className='mb-0'>{t('admin.courses.view_course.name')}</p>
              </div>
              <div className='col-sm-9'>
                <p className='text-muted mb-0'>{courseDetails.name}</p>
              </div>
            </div>
            <hr />
            <div className='row'>
              <div className='col-sm-3'>
                <p className='mb-0'>{t('admin.courses.view_course.level')}</p>
              </div>
              <div className='col-sm-9'>
                <p className='text-muted mb-0'>{courseDetails.level}</p>
              </div>
            </div>
            <hr />
            <div className='row'>
              <div className='col-sm-3'>
                <p className='mb-0'>
                  {t('admin.courses.view_course.description')}
                </p>
              </div>
              <div className='col-sm-9'>
                <p className='text-muted mb-0'>{courseDetails.description}</p>
              </div>
            </div>
            <hr />
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
                    {courseItems.length > 0 &&
                      courseItems
                        .slice(
                          page * rowsPerPage,
                          page * rowsPerPage + rowsPerPage
                        )
                        .map((item, index) => {
                          return (
                            <TableRow
                              hover
                              role='checkbox'
                              tabIndex={-1}
                              key={item.id}>
                              <TableCell>
                                {index +
                                  1 +
                                  (page * rowsPerPage, page * rowsPerPage)}
                              </TableCell>
                              <TableCell>{item.word}</TableCell>
                              <TableCell>{item.courseName}</TableCell>
                              <TableCell>
                                <Button
                                  size='small'
                                  variant='outlined'
                                  component={Link}
                                  to={`/courses/${courseDetails.id}/items/${item.id}`}
                                  endIcon={<ReadMoreIcon />}>
                                  {t('admin.courses.view_course.details')}
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
        </div>
      </div>
    </div>
  );
}
