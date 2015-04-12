from flask import Flask, request, url_for, render_template
import time, os
app = Flask(__name__)

# jekyll post params
LB = '\n'
Seperator = '---' + LB
Layout = 'layout: '
Title = 'title: '
Category = 'categories: '
Tags = 'tags: '
Excerpt = 'excerpt: '
Fpath = '../jekyll/_posts/'

# Fixing layout to post
Post = Seperator + Layout + 'post' + LB

@app.route('/', methods=['POST'])
def publish_post():
	nPost = Post
	nFname = time.strftime("%Y-%m-%d") + '-Log_' + time.strftime("%B-%d")

	# Title
	try:
		if not request.form['title']:
			raise Exception		
		nPost += Title + request.form['title'] + LB
		nFname = time.strftime("%Y-%m-%d") + '-' + request.form['title']
	except Exception, e:
		nPost += Title + "Log " + time.strftime("%B-%d") + LB

	# File name
	nFname = Fpath + nFname.replace(' ','_')
	i = 0
	while True:
		if not os.path.exists(nFname + '.md'):
			nFname = nFname + '.md'
			break
		else:
			i += 1; nFname += str(i)
			print nFname
			print str(i)

	# Categories
	try:
                if not request.form['cats']:
                        raise Exception
		nPost += Category + request.form['cats'] + LB
	except Exception, e:
		nPost += Category + 'Log ' + LB

	# Tags
	try:
                if not request.form['tags']:
                        raise Exception
		nPost += Tags + request.form['tags'] + LB
	except Exception, e:
		print 'no tags set'

	# Excerpt
	try:
                if not request.form['excerpt']:
                        raise Exception
		nPost += Excerpt + request.form['excerpt'] + LB
	except Exception, e:
		# Take excerpt as first 150 chars in post
		if not request.form['content']:
                        return 'No content sent. Aborted!'
		tContent = request.form['content']
		nPost += Excerpt + (request.form['content'])[0:148] + '...' + LB

	# Close header section
	nPost += LB + Seperator + LB

	# Content
	try:
                if not request.form['content']:
                        raise Exception
		nPost += request.form['content'] + LB
	except Exception, e:
		return 'No content sent. Aborted!'

	# Write to new post file
	nFile = open(nFname, 'w')
	nFile.write(nPost)
	nFile.close()

	# Build jekyll site
	os.system('jekyll  build -s ../jekyll -d ../public_html')

	return 'posted'

@app.route('/new', methods=['GET'])
def new_post():
	return render_template('new_post.html')

if __name__ == '__main__':
	app.debug = True
	app.run(host='0.0.0.0', port=2525)
