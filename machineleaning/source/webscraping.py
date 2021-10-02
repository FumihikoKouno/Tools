# coding: utf-8

import MeCab
import requests
import inspect
from bs4 import BeautifulSoup

WIKI_URL = 'https://ja.wikipedia.org'

if __name__ == '__main__':

    mt = MeCab.Tagger()
    result = requests.get(WIKI_URL + '/wiki/PlayStation_4%E3%81%AE%E3%82%B2%E3%83%BC%E3%83%A0%E3%82%BF%E3%82%A4%E3%83%88%E3%83%AB%E4%B8%80%E8%A6%A7')


    bs4 = BeautifulSoup(result.text, 'html.parser')
    title_list = []
    for table in bs4.find_all('table', {'class': 'sortable'}):
        for row in table.find_all('tr'):
            title = row.find_all(['td', 'th'])[1].find('a')
            if title is not None:
                if title['href'] is not None:
                    href = title['href']
                    if not href.startswith('#') and not href.startswith('/w/'):
                        title_list.append(title)

    for title in title_list:
        try:
            game = requests.get(WIKI_URL + title['href'])
        except:
            continue
        bs4 = BeautifulSoup(game.text, 'html.parser')
        print('checking %s...' % title.text)
        with open('ps4/%s.txt' % title.text.replace('/', '_'), 'w', encoding='utf-8') as file:
            file.write(bs4.find('div', {'id': 'content'}).text)

#    with open('test.csv', 'w', encoding='utf-8') as file:
#                for cell in row.find_all('a'):
#                    if 'href' in cell.attrs:
#                        href = cell.attrs['href']
#                        if not href.startswith('#') and not href.startswith('/w/'):
#                            print(cell.text)
#                            print(cell.attrs['href'])
#                            print()
