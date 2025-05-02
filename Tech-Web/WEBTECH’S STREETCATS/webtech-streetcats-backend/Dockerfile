# Use official Node LTS image
FROM node:latest
# Create app directory
WORKDIR /usr/src/app

# Install app dependencies
COPY package.json package-lock.json ./
RUN npm ci --only=production

# Copy source code
COPY . ./

# Build TypeScript (if applicable)
RUN npm run build

# Expose port and run
EXPOSE 3000
CMD ["node", "dist/server.js"]
