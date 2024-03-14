import Image from 'next/image';
import { Poppins } from 'next/font/google';

import { cn } from '@/lib/utils';

const font = Poppins({
  subsets: ['latin'],
  weight: ['400', '600'],
});

export const Logo = ({ scale }: { scale?: number }) => {
  const imageSize = scale ? 40 * scale : 40;

  return (
    <div className='hidden md:flex items-end gap-x-2'>
      <Image
        src='/logo.svg'
        height={imageSize}
        width={imageSize}
        alt='Logo'
        className='dark:hidden'
      />
      <Image
        src='/logo-dark.svg'
        height={imageSize}
        width={imageSize}
        alt='Logo'
        className='hidden dark:block'
      />
      <p className={cn('font-semibold', font.className)}>BuildMate</p>
    </div>
  );
};
