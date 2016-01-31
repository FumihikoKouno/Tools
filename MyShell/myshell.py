import os
import readline
import subprocess
import time

class LoggingShell(object):
    """ Logging shell class """

    def __init__(self, log_file_name,
                 prefix=None, time=False, elapsed=False):
        """ Init function of Logging shell.

            @params
            log_file_name: The output logging file.
            prefix: The prefix character of shell
            time: Output command input time
            elapsed: Output command elapsed time
        """
        self._file = open(log_file_name, 'w')
        if (prefix is None):
            self._prefix = '$ '
        else:
            self._prefix = prefix
        self._time = time
        self._elapsed = elapsed
    
    def __del__(self):
        self._file.close()

    def _execute(self, command):
        """ Execute command by shell and return (stdout, stderr). """
        p = subprocess.Popen(command,
                             shell=True,
                             stdin=subprocess.PIPE,
                             stdout=subprocess.PIPE,
                             stderr=subprocess.PIPE)
        return p.communicate()

    def write_input(self, command):
        """ Write command input to logging file. """
        self._file.write(self._prefix + command)
        if (self._time):
            time_string = time.strftime('%Y/%m/%d %H:%M:%S', time.localtime())
            self._file.write(' [%(time)s]' % {'time': time_string})
        self._file.write('\n')

    def write_output(self, results):
        """ Write command result to logging file. """
        for result in results:
            if (result != ''):
                self._file.write(result)
                self._file.write('\n')
                print result
        if (self._elapsed):
            elapsed_time = self._end_time - self._start_time
            elapsed_output = 'Elapsed time: %(time)ss' % {'time': elapsed_time}
            self._file.write(elapsed_output)
            self._file.write('\n')
            print elapsed_output
        self._file.write('\n')
        print

    def complete(self, text, state):
        if ('/' in text):
            return self._complete_directory(text, state)
        else:
            return self._complete_commands(text, state)

    def _search_match_range(self, text, texts):
        if (text == ''):
            return 0, len(texts)
        match_start = 0
        match_end = 0
        start = 0
        end = len(texts)
        mid = (start + end) / 2
        while start < end:
            if (texts[mid].startswith(text)):
                match_start = mid - 1
                match_end = mid + 1
                while texts[match_start].startswith(text):
                    match_start = match_start - 1
                while texts[match_end].startswith(text):
                    match_end = match_end + 1
                match_start = match_start + 1
                break
            else:
                if (texts[mid] < text):
                    start = mid + 1
                    mid = (start + end) / 2
                else:
                    end = mid
                    mid = (start + end) / 2
        if (match_start == match_end):
            return None, None
        return match_start, match_end

    def _complete_directory(self, text, state):
        if (state > 0):
            match_state = self._match_start + state
            if (match_state < self._match_end):
                return self._search_directory_name + \
                       self._in_search_dir[match_state]
        else:
            final_slash_pos = text.rfind('/')
            self._search_directory_name = text[:final_slash_pos + 1]
            if (text.endswith('/')):
                file_name = ''
            else:
                file_name = text[final_slash_pos + 1:]
            out, err = \
                self._execute('ls %(dir)s' % \
                {'dir': self._search_directory_name})
            self._in_search_dir = out.split('\n')
            self._match_start, self._match_end = \
                self._search_match_range(file_name, self._in_search_dir)
            if (self._match_start is not None):
                return self._search_directory_name + \
                       self._in_search_dir[self._match_start]
        return None

    def _complete_commands(self, text, state):
        """ Return completion string. """
        if (state > 0):
            match_state = self._match_start + state
            if (match_state < self._match_end):
                return self._commands[match_state]
        else:
            self._match_start, self._match_end = \
                self._search_match_range(text, self._commands)
            if (self._match_start is not None):
                return self._commands[self._match_start]
        return None

    def run(self):
        """ Start logging shell. """
        exit_commands = ['exit', 'Exit', 'EXIT', 'q', 'quit', 'Quit']
        pathes = os.getenv('PATH').split(':')
        pathes.append('./')
        self._commands = []
        for path in pathes:
            out, err = self._execute('ls %(path)s' % {'path': path})
            self._commands.extend(out.split('\n'))
        self._commands.extend(exit_commands)
        self._commands.sort()
        readline.set_completer(self.complete)
        original_delims = readline.get_completer_delims()
        new_delims = original_delims.replace('.', '').replace('/', '')
        readline.set_completer_delims(new_delims)
        print 'Start logging to "%(file)s"' % {'file': self._file.name}
        print 'exit logging by typing one of %(exit)s' % \
              {'exit': exit_commands}
        readline.parse_and_bind('tab: complete')
        stdin = raw_input(self._prefix)
        while (stdin not in exit_commands):
            try:
                self.execute(stdin)
                stdin = raw_input(self._prefix)
            except EOFError:
                print 'The input file has finished reading.'
                break
        print 'End logging. Log file is "%(file)s"' % {'file': self._file.name}

    def execute(self, command):
        """ Call log write functions and command execute function. """
        self.write_input(command)
        self._start_time = time.time()
        results = self._execute(command)
        self._end_time = time.time()
        self.write_output(results)

