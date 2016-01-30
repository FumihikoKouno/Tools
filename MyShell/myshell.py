import readline
import sys
import cStringIO
import subprocess
import argparse
import time

arg_parser = argparse.ArgumentParser()
arg_parser.add_argument('-p', '--prefix', 
                        help='Specify prefix character.: default "$"')
arg_parser.add_argument('-t', '--time',
                        action='store_true',
                        help='Print command input time.')
arg_parser.add_argument('-e', '--elapsed',
                        action='store_true',
                        help='Print command execute time.')
arg_parser.add_argument('log_file')
args = arg_parser.parse_args()


if (args.prefix is not None):
    STDIN_PREFIX = args.prefix
else:
    STDIN_PREFIX = '$ '


class LoggingShell(object):

    def __init__(self, log_file_name):
        self._file = open(log_file_name, 'w')
    
    def __del__(self):
        self._file.close()

    def _execute(self, command):
        p = subprocess.Popen(command,
                             shell=True,
                             stdin=subprocess.PIPE,
                             stdout=subprocess.PIPE,
                             stderr=subprocess.PIPE)
        return p.communicate()

    def write_input(self, command):
        self._file.write(STDIN_PREFIX + command)
        if (args.time):
            time_string = time.strftime('%Y/%m/%d %H:%M:%S', time.localtime())
            self._file.write(' [%(time)s]' % {'time': time_string})
        self._file.write('\n')

    def write_output(self, results):
        for result in results:
            if (result != ''):
                self._file.write(result)
                self._file.write('\n')
                print result
        if (args.elapsed):
            elapsed_time = self._end_time - self._start_time
            elapsed_output = 'Elapsed time: %(time)ss' % {'time': elapsed_time}
            self._file.write(elapsed_output)
            self._file.write('\n')
            print elapsed_output

    def execute(self, command):
        self.write_input(command)
        self._start_time = time.time()
        results = self._execute(command)
        self._end_time = time.time()
        self.write_output(results)


exit_commands = ['exit', 'Exit', 'EXIT', 'q', 'quit', 'Quit']

print 'Start logging to "%(file)s"' % {'file': args.log_file}
print 'exit logging by typing one of %(exit)s' % {'exit': exit_commands}

shell = LoggingShell(args.log_file)


stdin = raw_input(STDIN_PREFIX)
while (stdin not in exit_commands):
    try:
        shell.execute(stdin)
        stdin = raw_input(STDIN_PREFIX)
    except EOFError:
        print 'The input file has finished reading.'
        break

print 'End logging. Log file is "%(file)s"' % {'file': args.log_file}
