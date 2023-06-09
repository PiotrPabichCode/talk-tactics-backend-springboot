export const convertFinishTime = (time) => {
  const year = time[0];
  const month = time[1] - 1;
  const day = time[2];
  const hours = time[3];
  const minutes = time[4];
  const seconds = time[5];

  const formattedTime = `${day}/${month}/${year} ${hours}:${minutes}:${seconds}`;
  return formattedTime;
};
