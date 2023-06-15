export const levels = [
  { value: 'BEGINNER' },
  { value: 'INTERMEDIATE' },
  { value: 'ADVANCED' },
];

export const renderLevel = (level, t) => {
  switch (level) {
    case 'BEGINNER':
      return t('admin.courses.levels.beginner');
    case 'INTERMEDIATE':
      return t('admin.courses.levels.intermediate');
    case 'ADVANCED':
      return t('admin.courses.levels.advanced');
    default:
      return null;
  }
};
