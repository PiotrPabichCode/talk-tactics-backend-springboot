import { Button } from '@/components/ui/button';
import { Footer } from './_components/footer';
import { Logo } from './_components/logo';
import { ArrowRight } from 'lucide-react';

export default function LandingPage() {
  return (
    <div className='flex flex-1 flex-col'>
      <div className='mx-auto max-w-2xl flex flex-1 flex-col gap-10 items-center'>
        <Logo scale={3} />
        <div className='text-center'>
          <h1 className='text-4xl font-bold tracking-tight  sm:text-6xl'>
            Discover the Power of Building with Expertise
          </h1>
          <p className='mt-6 text-lg leading-8 '>
            Find skilled builders for your dream project.{' '}
            <strong>BuildMate</strong> - Building Dreams Together.
          </p>
          <div className='mt-8 flex items-center justify-center gap-x-6'>
            <Button className='rounded-md bg-indigo-600 px-3.5 py-2.5 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600'>
              Get started
            </Button>
            <Button className='text-sm font-semibold leading-6 flex items-center gap-2'>
              Learn more <ArrowRight />
            </Button>
          </div>
        </div>
      </div>
      <Footer />
    </div>
  );
}
