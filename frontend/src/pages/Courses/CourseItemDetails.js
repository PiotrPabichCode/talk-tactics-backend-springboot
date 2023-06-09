import * as React from 'react';
import Paper from '@mui/material/Paper';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import { Card, CardContent, TableSortLabel, Typography } from '@mui/material';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { request } from '../../api/AxiosHelper';
import { Button } from 'bootstrap';

const columns = [
  { id: 'id', label: 'ID', minWidth: 50 },
  { id: 'definition', label: 'Definition', minWidth: 250 },
  { id: 'example', label: 'Example', minWidth: 250 },
];

export default function CourseItemDetails(match) {
  // React.useEffect(() => {
  //   console.log(item);
  // }, [item]);

  const params = useParams();
  const { id } = params;
  const navigate = useNavigate();

  const [itemDetails, setItemDetails] = React.useState({});

  React.useEffect(() => {
    const loadCourseItemDetails = async () => {
      try {
        const response = await request('GET', `/api/course-items/${id}`);
        console.log(response.data);
        setItemDetails(response.data);
      } catch (error) {
        console.log(error);
      }
    };
    loadCourseItemDetails();
  }, []);

  return (
    itemDetails && (
      <div className='d-flex justify-content-center m-5'>
        {/* <Button variant='outlined'>Link</Button> */}
        <Card>
          <div style={{ display: 'flex', justifyContent: 'flex-end' }}>
            <Link className='btn btn-primary m-2' to={() => navigate(-1)}>
              Back
            </Link>
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
            <Paper alignCenter sx={{ width: '100%', overflow: 'hidden' }}>
              <TableContainer>
                <Table aria-label='sticky table'>
                  <TableHead>
                    <TableRow>
                      {columns.map((column) => (
                        <TableCell
                          key={column.id}
                          align={'center'}
                          style={{ minWidth: column.minWidth }}>
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
                            key={index}>
                            <TableCell key={index}>{index + 1}</TableCell>
                            <TableCell key={index}>
                              {meaning.definition}
                            </TableCell>
                            <TableCell key={index} align='center'>
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
