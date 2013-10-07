import storm
from bs4 import BeautifulSoup
from urlparse import urlparse
import urllib2
import json

class ViningSpout(storm.Spout):  
    def initialize(self, conf, context):
        pass
    
    def nextTuple(self):
        try:       
            url_all_tl = 'http://www.pinterest.com/all/'
            url_animals_tl = 'http://www.pinterest.com/all/animals/'
            pin_prefix = 'http://www.pinterest.com/pin/'
 
            html = urllib2.urlopen(url_all_tl).read()
            soup = BeautifulSoup(html)
            scripts = soup.find_all('script')
            code = scripts[len(scripts) - 1]

            content = code.contents[0].strip()
            prefix = 'P.start.start('
            i = content.find(prefix)
            if i != -1:
                json_str = content[i + len(prefix) : len(content) - 2]
                json_obj = json.loads(json_str)
                pins = json_obj['tree']['children'][3]['children'][0]['children'][0]['children']
                
                for pin in pins:
                    if 'module' in pin['children'][1]['options']:
                        module = pin['children'][1]['options']['module']
                        #print 'pin desciption', module['options']['pin_description']
                    #print 'pin id', pin_prefix + pin['options']['pin_id']
                    orig_link = pin['data']['link']
                    orig_host = urlparse(pin['data']['link']).hostname
                    images = pin['data']['images']
                    if 'orig' in images:
                        #print 'orig', images['orig']['url']
                        pass
                    storm.emit([pin['options']['pin_id'], orig_link, orig_host, json.dumps(pin, indent=4, sort_keys=True)])
                
            time.sleep(2)
        except StopIteration:
            pass
        except urllib2.HTTPError, err:
            if err.code == 404:
                pass
        except:
            pass
ViningSpout().run()




