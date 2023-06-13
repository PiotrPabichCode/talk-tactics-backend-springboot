import * as React from 'react';
import Paper from '@mui/material/Paper';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import { Card, CardContent, TableSortLabel, Typography } from '@mui/material';
import { Link, useParams, useNavigate } from 'react-router-dom';
import useCourseItemDetails from './hooks/useCourseItemDetails';
import { useTranslation } from 'react-i18next';

export default function CourseItemDetails(id) {
  const { t } = useTranslation();
  const { itemID } = useParams();
  const itemDetails = useCourseItemDetails(itemID);
  const navigate = useNavigate();

  const columns = [
    { id: 'id', label: 'ID', minWidth: 50 },
    {
      id: 'definition',
      label: t('courses.course_item_details.header_definition'),
      minWidth: 250,
    },
    {
      id: 'example',
      label: t('courses.course_item_details.header_example'),
      minWidth: 250,
    },
  ];

  return (
    itemDetails.course && (
      <div className='d-flex justify-content-center m-5'>
        <Card>
          <div style={{ display: 'flex', justifyContent: 'flex-end' }}>
            <button
              className='btn btn-primary m-2'
              onClick={() => navigate(-1)}>
              {t('courses.course_item_details.back')}
            </button>
          </div>
          <CardContent>
            <Typography variant='h4' component='div'>
              {itemDetails.word}
            </Typography>
            <Typography
              sx={{ fontSize: 14 }}
              color='text.secondary'
              gutterBottom>
              {itemDetails.phonetic}
            </Typography>
            <Typography sx={{ mb: 1.5 }} color='text.secondary'>
              {itemDetails.partOfSpeech}
            </Typography>
            <Typography sx={{ mb: 1.5, fontSize: 18 }} color='text.secondary'>
              {itemDetails.course.name}
            </Typography>
            <Paper sx={{ width: '100%', overflow: 'hidden' }}>
              <TableContainer>
                <Table aria-label='sticky table'>
                  <TableHead>
                    <TableRow>
                      {columns.map((column, index) => (
                        <TableCell
                          align={'center'}
                          style={{ minWidth: column.minWidth }}
                          key={index}>
                          {column.label}
                        </TableCell>
                      ))}
                    </TableRow>
                  </TableHead>
                  <TableBody>
                    {itemDetails.meanings &&
                      itemDetails.meanings.map((meaning, index) => {
                        return (
                          <TableRow
                            hover
                            role='checkbox'
                            tabIndex={-1}
                            key={meaning.id}>
                            <TableCell>{index + 1}</TableCell>
                            <TableCell>{meaning.definition}</TableCell>
                            <TableCell align='center'>
                              {meaning.example ? meaning.example : '-'}
                            </TableCell>
                          </TableRow>
                        );
                      })}
                  </TableBody>
                </Table>
              </TableContainer>
            </Paper>
          </CardContent>
        </Card>
      </div>
    )
  );
}
