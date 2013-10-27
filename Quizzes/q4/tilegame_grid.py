class Grid(object):
  '''
  Grid class
  '''
  # (0,0) assumed to be top-left  

  def __init__(self, size):
    self.size = size
    self.grid = [[None for x in xrange(size)] for x in xrange(size)]
    self.queue_fillall_flag = False

  def __setitem__(self, co, t):
    '''
    Usage can be either:
      some_grid[(x, y)] = t
      some_grid[x][y] = t
    Using the first variation will call this method.
    The second uses __getitem__.
    '''
    self.grid[co[0]][co[1]] = t

  def __getitem__(self, co):
    '''
    Usage can be either:
      t = some_grid[(x, y)]
      t = some_grid[x][y]
    '''
    if type(co) in [list, tuple]:
      # passed a set of coordinates; using first variation above
      return self.grid[co[0]][co[1]] 
    if type(co) is int:
      # just given the x coordinate; using second variation above
      return self.grid[co]
    else:
      raise KeyError('Invalid coordinates ' + str(co))

  # Methods assumed to be implemented:
  def neighbors(self, tile):
    ''' Returns all tiles adjacent to the given tile. '''
    return [ self[x][y]
               for x in [tile.x + 1, tile.x - 1]
               for y in [tile.y + 1, tile.y - 1]
               if self[x][y] ]

  def queue_fillall(self):
    '''
    Queues an operation to fill all holes at the end of this frame.
    If such an operation has already been queued, no action is performed.
    '''
    self.queue_fillall_flag = True

  def advance_frame(self):
    '''
    Performs frame advancement operations. This should be called exactly once per frame.
    '''
    if self.queue_fillall_flag:
        self.fill_all_holes()    # see below for implementation
        
        
# Operations
def process_tile(grid, tile):
  valid = [t for t in grid.neighbors(tile) if x and x.color is tile.color]
  grid[(tile.x, tile.y)] = None   # Clear this tile
  for t in valid:
    grid.process_tile(tile)
  grid.queue_filllall()
  
def fill_hole(grid, co):
  drop = 0
  for y in range(co.y, 0, -1):
    t = grid[(t.x, t.y)]
    if t is None:
      drop += 1
    else:
      t.y += drop
      grid[(t.x, t.y)] = t
  y = 0

  # Fill down all empty holes until reaching a full one
  # (semantically this is wrong to fill the top tiles first, but it doesn't matter)
  while not grid[(co.x, y)]:
    grid[(co.x, y)] = create_random_tile()  # Assumed to create a tile with random color
    y += 1

def fill_all_holes(grid):
  fill_hole(grid, (x, grid.size - 1)) for x in range(grid.size)
